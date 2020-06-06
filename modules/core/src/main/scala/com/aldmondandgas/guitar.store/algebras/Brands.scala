package com.aldmondandgas.guitar.store.algebras
import com.aldmondandgas.guitar.store.entities.brand.{Brand, BrandName}

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[Unit]
}
