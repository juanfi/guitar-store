package com.aldmondandgas.guitar.store.entities
import squants.market.Money

final case class Payment(id: UserId, total: Money, card: Card)
