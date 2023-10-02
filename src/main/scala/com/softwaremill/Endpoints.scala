package com.softwaremill

import sttp.tapir._
import Library._
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.Task
import zio.ZIO

object Endpoints {
  case class User(name: String) extends AnyVal
  val helloEndpoint: PublicEndpoint[User, Unit, String, Any] = endpoint.get
    .in("hello")
    .in(query[User]("name"))
    .out(stringBody)
  val helloServerEndpoint: ZServerEndpoint[Any, Any] = helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user.name}"))

  val booksListing: PublicEndpoint[Unit, Unit, List[Book], Any] = endpoint.get
    .in("books" / "list" / "all")
    .out(jsonBody[List[Book]])
  val booksListingServerEndpoint: ZServerEndpoint[Any, Any] = booksListing.serverLogicSuccess(_ => ZIO.succeed(Library.books))

  val apiEndpoints: List[ZServerEndpoint[Any, Any]] = List(helloServerEndpoint, booksListingServerEndpoint)

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = RedocInterpreter().fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints
}

object Library {
  case class CcA(name: String)
  case class CcB(name: String)
  case class CcC(name: String)
  case class CcD(name: String)

  case class Book(
      title: String,
      year: Int,
      a1: CcA,
      a2: CcA,
      @Schema.annotations.deprecated b1: CcB,
      b2: CcB,
      c1: CcC,
      @Schema.annotations.deprecated c2: CcC,
      @Schema.annotations.deprecated d1: CcD,
      @Schema.annotations.deprecated d2: CcD
  )

  val books = List(
    Book(
      "A The Sorrows of Young Werther",
      1774,
      CcA("A a1"),
      CcA("A a2"),
      CcB("A b1"),
      CcB("A b2"),
      CcC("A c1"),
      CcC("A c2"),
      CcD("A d1"),
      CcD("A d2")
    ),
    Book(
      "C The Art of Computer Programming",
      1968,
      CcA("C a1"),
      CcA("C a2"),
      CcB("C b1"),
      CcB("C b2"),
      CcC("C c1"),
      CcC("C c2"),
      CcD("C d1"),
      CcD("C d2")
    )
  )

}
