package com.aldmondandgas.guitar.store.algebras

import com.aldmondandgas.guitar.store.entities.{Category, CategoryName}

trait Categories[F[_]] {
  def findAll: F[List[Category]]
  def create(name: CategoryName): F[Unit]
}