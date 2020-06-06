package com.aldmondandgas.guitar.store.entities

import java.util.UUID

import io.estatico.newtype.macros.newtype

object brand {
  @newtype case class BrandId(value: UUID)
  @newtype case class BrandName(value: String)

  final case class Brand(uuid: BrandId, name: BrandName)
}
