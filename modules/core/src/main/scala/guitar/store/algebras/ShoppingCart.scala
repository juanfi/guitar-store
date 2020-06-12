package guitar.store.algebras

import guitar.store.domain.auth.UserId
import guitar.store.domain.cart.{ Cart, CartTotal, Quantity }
import guitar.store.domain.item.ItemId
import guitar.store.domain.auth.UserId
import guitar.store.domain.cart.{ Cart, CartTotal, Quantity }
import guitar.store.domain.item.ItemId

trait ShoppingCart[F[_]] {
  def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit]
  def delete(userId: UserId): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, cart: Cart): F[Unit]
}
