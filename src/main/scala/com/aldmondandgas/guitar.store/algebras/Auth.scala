package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.{
  JwtToken,
  Password,
  User,
  UserName
}

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}
