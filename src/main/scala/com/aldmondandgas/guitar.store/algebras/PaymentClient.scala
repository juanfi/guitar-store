package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.{Payment, PaymentId}

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}
