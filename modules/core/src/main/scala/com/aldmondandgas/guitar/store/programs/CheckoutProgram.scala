package com.aldmondandgas.guitar.store.programs

import cats.MonadError
import com.aldmondandgas.guitar.store.effects.Background
import com.aldmondandgas.guitar.store.entities.auth.UserId
import com.aldmondandgas.guitar.store.entities.card.Card
import com.aldmondandgas.guitar.store.entities.cart.CartItem
import com.aldmondandgas.guitar.store.entities.order.{ OrderError, OrderId, PaymentError, PaymentId }
import com.aldmondandgas.guitar.store.entities.payment.Payment
import io.chrisdavenport.log4cats.Logger
//import javafx.scene.layout.Background
import squants.market.Money
//import cats.implicits._
import cats.syntax.applicativeError._
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.semigroup._
import com.aldmondandgas.guitar.store.algebras.{ Orders, PaymentClient, ShoppingCart }
import retry.RetryDetails._
import retry.RetryPolicies._
import retry._

import scala.concurrent.duration._

final class CheckoutProgram[F[_]: MonadError[F, Throwable]: Logger: Background](
    paymentClient: PaymentClient[F],
    shoppingCart: ShoppingCart[F],
    orders: Orders[F]
) {

  val retryPolicy = limitRetries[F](3) |+| exponentialBackoff[F](10.milliseconds)

  def logError(action: String)(e: Throwable, details: RetryDetails): F[Unit] = details match {
    case r: WillDelayAndRetry => Logger[F].error(s"Failed on $action. We retried ${r.retriesSoFar} times.")
    case g: GivingUp          => Logger[F].error(s"Giving up on $action after ${g.totalRetries} retries.")
  }

  def processPayment(payment: Payment): F[PaymentId] = {
    val action = retryingOnAllErrors[PaymentId](
      policy = retryPolicy,
      onError = logError("Payments")
    )(paymentClient.process(payment))

    action.adaptErr {
      case e => PaymentError(Option(e.getMessage).getOrElse("Unknown"))
    }
  }

  def createOrder(userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money): F[OrderId] = {
    val action = retryingOnAllErrors[OrderId](policy = retryPolicy, onError = logError("Order"))(
      orders.create(userId, paymentId, items, total)
    )

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.adaptErr {
          case e => OrderError(e.getMessage)
        }
        .onError {
          case _ =>
            Logger[F].error(s"Failed to create order for: $paymentId") *> Background[F].schedule(bgAction(fa), 1.hour)
        }

    bgAction(action)
  }

  def checkout(userId: UserId, card: Card): F[OrderId] =
    for {
      cart <- shoppingCart.get(userId)
      paymentId <- paymentClient.process(Payment(userId, cart.total, card))
      orderId <- orders.create(userId, paymentId, cart.items, cart.total)
      _ <- shoppingCart.delete(userId).attempt.void
    } yield orderId

}
