package guitar.store.http.routes

import cats.{ Defer, Monad }
import guitar.store.algebras.Brands
import guitar.store.http.json._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class BrandRoutes[F[_]: Defer: Monad](brands: Brands[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => Ok(brands.findAll)
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
