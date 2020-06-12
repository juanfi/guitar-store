package guitar.store.algebras

import guitar.store.domain.order.PaymentId
import guitar.store.domain.payment.Payment

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}
