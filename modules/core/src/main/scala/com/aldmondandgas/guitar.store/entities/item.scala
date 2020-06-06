package com.aldmondandgas.guitar.store.entities

import java.util.UUID

import com.aldmondandgas.guitar.store.entities.brand.{Brand, BrandId}
import com.aldmondandgas.guitar.store.entities.category.{Category, CategoryId}
import io.estatico.newtype.macros.newtype
import squants.market.Money

object item {
  @newtype case class ItemId(value: UUID)
  @newtype case class ItemName(value: String)
  @newtype case class ItemDescription(value: String)

  final case class Item(uuid: ItemId,
                        name: ItemName,
                        description: ItemDescription,
                        price: Money,
                        brand: Brand,
                        category: Category)

  final case class CreateItem(name: ItemName,
                              description: ItemDescription,
                              price: Money,
                              brandId: BrandId,
                              categoryId: CategoryId)

  final case class UpdateItem(id: ItemId, price: Money)
}
