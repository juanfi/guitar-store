package guitar.store.domain

import java.util.UUID

import guitar.store.domain.cart.Quantity
import guitar.store.domain.item.ItemId
import guitar.store.domain.cart.Quantity
import io.estatico.newtype.macros.newtype
import squants.market.Money

import scala.util.control.NoStackTrace

object order {
  @newtype case class OrderId(valie: UUID)
  @newtype case class PaymentId(valie: UUID)

  final case class Order(id: OrderId, pid: PaymentId, items: Map[ItemId, Quantity], total: Money)

  final case class OrderError(cause: String) extends NoStackTrace
  final case class PaymentError(cause: String) extends NoStackTrace
  case object EmptyCartError extends NoStackTrace
}
