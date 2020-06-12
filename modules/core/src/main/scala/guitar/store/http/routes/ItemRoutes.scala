package guitar.store.http.routes

import cats.{ Defer, Monad }
import guitar.store.algebras.Items
import guitar.store.domain.brand.BrandParam
import guitar.store.http.json._
import guitar.store.http.params._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class ItemRoutes[F[_]: Defer: Monad](items: Items[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/items"

  object BrandQueryParam extends OptionalQueryParamDecoderMatcher[BrandParam]("brand")

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? BrandQueryParam(brand) => Ok(brand.fold(items.findAll)(b => items.findBy(b.toDomain)))
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
