package com.aldmondandgas.guitar.store.entities

import java.util.UUID

import io.estatico.newtype.macros.newtype

object category {
  @newtype case class CategoryId(value: UUID)
  @newtype case class CategoryName(value: String)

  final case class Category(uuid: CategoryId, name: CategoryName)
}
