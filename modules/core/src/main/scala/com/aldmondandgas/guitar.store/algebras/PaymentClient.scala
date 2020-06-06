package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.order.PaymentId
import com.aldmondandgas.guitar.store.entities.payment.Payment

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}
