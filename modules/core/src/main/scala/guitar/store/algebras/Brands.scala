package guitar.store.algebras

import guitar.store.domain.brand.{ Brand, BrandName }

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[Unit]
}
