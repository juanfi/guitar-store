package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.{
  CartItem,
  Order,
  OrderId,
  PaymentId,
  UserId
}
import squants.market.Money

trait Orders[F[_]] {
  def get(userId: UserId, orderId: OrderId): F[Option[Order]]
  def findBy(userId: UserId): F[List[Order]]
  def create(userId: UserId,
             paymentId: PaymentId,
             items: List[CartItem],
             total: Money): F[OrderId]
}
