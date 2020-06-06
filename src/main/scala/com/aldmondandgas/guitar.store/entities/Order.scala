package com.aldmondandgas.guitar.store.entities
import java.util.UUID

import io.estatico.newtype.macros.newtype
import squants.market.Money

@newtype case class OrderId(valie: UUID)
@newtype case class PaymentId(valie: UUID)

final case class Order(id: OrderId,
                       pid: PaymentId,
                       items: Map[ItemId, Quantity],
                       total: Money)
