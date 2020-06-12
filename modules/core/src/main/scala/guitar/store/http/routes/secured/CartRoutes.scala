package guitar.store.http.routes.secured
import cats.implicits._
import cats.{ Defer, Monad }
import guitar.store.algebras.ShoppingCart
import guitar.store.domain.cart.Cart
import guitar.store.domain.item.ItemId
import guitar.store.http.auth.users.CommonUser
import guitar.store.http.json._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{ AuthMiddleware, Router }
import org.http4s.{ AuthedRoutes, HttpRoutes }

final class CartRoutes[F[_]: Defer: JsonDecoder: Monad](shoppingCart: ShoppingCart[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/cart"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {
    case GET -> Root as user => Ok(shoppingCart.get(user.value.id))

    case ar @ POST -> Root as user =>
      ar.req.asJsonDecode[Cart].flatMap { cart =>
        cart.items
          .map {
            case (id, quantity) => shoppingCart.add(user.value.id, id, quantity)
          }
          .toList
          .sequence *> Created()
      }

    case ar @ PUT -> Root as user =>
      ar.req.asJsonDecode[Cart].flatMap { cart =>
        shoppingCart.update(user.value.id, cart) *> Ok()
      }

    case DELETE -> Root / UUIDVar(uuid) as user =>
      shoppingCart.removeItem(user.value.id, ItemId(uuid)) *> NoContent()
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
