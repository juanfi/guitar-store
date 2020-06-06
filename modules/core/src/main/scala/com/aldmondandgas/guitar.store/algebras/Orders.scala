package com.aldmondandgas.guitar.store.algebras

import com.aldmondandgas.guitar.store.entities.auth.UserId
import com.aldmondandgas.guitar.store.entities.cart.CartItem
import com.aldmondandgas.guitar.store.entities.order.{Order, OrderId, PaymentId}
import squants.market.Money

trait Orders[F[_]] {
  def get(userId: UserId, orderId: OrderId): F[Option[Order]]
  def findBy(userId: UserId): F[List[Order]]
  def create(userId: UserId,
             paymentId: PaymentId,
             items: List[CartItem],
             total: Money): F[OrderId]
}
