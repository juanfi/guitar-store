package com.aldmondandgas.guitar.store.programs
import cats.Monad
import cats.implicits._
import com.aldmondandgas.guitar.store.algebras.{
  Orders,
  PaymentClient,
  ShoppingCart
}
import com.aldmondandgas.guitar.store.entities.{Card, OrderId, Payment, UserId}

final class CheckoutProgram[F[_]: Monad](paymentClient: PaymentClient[F],
                                         shoppingCart: ShoppingCart[F],
                                         orders: Orders[F]) {
  def checkout(userId: UserId, card: Card): F[OrderId] =
    for {
      cart <- shoppingCart.get(userId)
      paymentId <- paymentClient.process(Payment(userId, cart.total, card))
      orderId <- orders.create(userId, paymentId, cart.items, cart.total)
      _ <- shoppingCart.delete(userId)
    } yield orderId

}
