package guitar.store.algebras

import guitar.store.domain.auth.{ Password, User, UserId, UserName }
import guitar.store.domain.auth.{ Password, User, UserId, UserName }

trait Users[F[_]] {
  def find(username: UserName, password: Password): F[Option[User]]
  def create(username: UserName, password: Password): F[UserId]
}
