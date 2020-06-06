package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.auth.{ Password, User, UserId, UserName }

trait Users[F[_]] {
  def find(username: UserName, password: Password): F[Option[User]]
  def create(username: UserName, password: Password): F[UserId]
}
