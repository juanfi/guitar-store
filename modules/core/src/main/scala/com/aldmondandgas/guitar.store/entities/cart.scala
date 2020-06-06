package com.aldmondandgas.guitar.store.entities
import java.util.UUID

import com.aldmondandgas.guitar.store.entities.item.{Item, ItemId}
import io.estatico.newtype.macros.newtype
import squants.market.Money

object cart {
  @newtype case class Quantity(value: Int)
  @newtype case class CartId(value: UUID)
  @newtype case class Cart(items: Map[ItemId, Quantity])

  final case class CartItem(item: Item, quantity: Quantity)
  final case class CartTotal(items: List[CartItem], total: Money)
}
