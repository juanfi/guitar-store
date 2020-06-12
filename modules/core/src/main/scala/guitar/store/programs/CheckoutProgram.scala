package guitar.store.programs

import cats.effect.Timer
import cats.syntax.applicativeError._
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.monadError._
import guitar.store.algebras.{ Orders, PaymentClient, ShoppingCart }
import guitar.store.effects.{ Background, MonadThrow }
import guitar.store.domain.auth.UserId
import guitar.store.domain.card.Card
import guitar.store.domain.cart.{ CartItem, CartTotal }
import guitar.store.domain.order._
import guitar.store.domain.payment.Payment
import guitar.store.algebras.{ Orders, PaymentClient }
import guitar.store.effects.Background
import guitar.store.domain.auth.UserId
import guitar.store.domain.card.Card
import guitar.store.domain.cart.CartItem
import guitar.store.domain.order.{ EmptyCartError, OrderId, PaymentId }
import guitar.store.domain.payment.Payment
import io.chrisdavenport.log4cats.Logger
import retry.RetryDetails._
import retry._
import squants.market.Money

import scala.concurrent.duration._

final class CheckoutProgram[F[_]: Background: Logger: MonadThrow: Timer](
    paymentClient: PaymentClient[F],
    shoppingCart: ShoppingCart[F],
    orders: Orders[F],
    retryPolicy: RetryPolicy[F]
) {

//  val retryPolicy = limitRetries[F](3) |+| exponentialBackoff[F](10.milliseconds)

  def logError(action: String)(e: Throwable, details: RetryDetails): F[Unit] = details match {
    case r: WillDelayAndRetry => Logger[F].error(e)(s"Failed on $action. We retried ${r.retriesSoFar} times.")
    case g: GivingUp          => Logger[F].error(e)(s"Giving up on $action after ${g.totalRetries} retries.")
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
    val action = retryingOnAllErrors[OrderId](
      policy = retryPolicy,
      onError = logError("Order")
    )(
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
    shoppingCart
      .get(userId)
      .ensure(EmptyCartError)(_.items.nonEmpty)
      .flatMap {
        case CartTotal(items, total) =>
          for {
            paymentId <- processPayment(Payment(userId, total, card))
            order <- createOrder(userId, paymentId, items, total)
            _ <- shoppingCart.delete(userId).attempt.void
          } yield order
      }

}
