package com.aldmondandgas.guitar.store.entities
import java.util.UUID

import io.estatico.newtype.macros.newtype

object auth {
  @newtype case class UserId(value: UUID)
  @newtype case class UserName(value: String)
  @newtype case class Password(value: String)
  @newtype case class JwtToken(value: String)

  final case class User(id: UserId, name: UserName)
}
