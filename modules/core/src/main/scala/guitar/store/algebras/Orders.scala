package guitar.store.algebras

import guitar.store.domain.auth.UserId
import guitar.store.domain.cart.CartItem
import guitar.store.domain.order.{ Order, OrderId, PaymentId }
import guitar.store.domain.auth.UserId
import squants.market.Money

trait Orders[F[_]] {
  def get(userId: UserId, orderId: OrderId): F[Option[Order]]
  def findBy(userId: UserId): F[List[Order]]
  def create(userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money): F[OrderId]
}
