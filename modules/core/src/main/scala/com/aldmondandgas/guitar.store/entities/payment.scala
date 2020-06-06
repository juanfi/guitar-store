package com.aldmondandgas.guitar.store.entities
import com.aldmondandgas.guitar.store.entities.auth.UserId
import com.aldmondandgas.guitar.store.entities.card.Card
import squants.market.Money

object payment {
  final case class Payment(id: UserId, total: Money, card: Card)
}
