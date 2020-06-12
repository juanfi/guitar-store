package guitar.store.algebras

import guitar.store.domain.category.{ Category, CategoryName }

trait Categories[F[_]] {
  def findAll: F[List[Category]]
  def create(name: CategoryName): F[Unit]
}
