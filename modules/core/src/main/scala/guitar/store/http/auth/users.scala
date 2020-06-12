package guitar.store.http.auth

import dev.profunktor.auth.jwt._
import guitar.store.domain.auth.{ UserId, UserName }
import io.estatico.newtype.macros.newtype

object users {

  @newtype case class AdminJwtAuth(value: JwtSymmetricAuth)
  @newtype case class UserJwtAuth(value: JwtSymmetricAuth)

  case class User(id: UserId, name: UserName)

  @newtype case class CommonUser(value: User)
  @newtype case class AdminUser(value: User)

}
