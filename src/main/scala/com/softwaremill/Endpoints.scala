package com.softwaremill

import sttp.tapir._
import Library._
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
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

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = RedocInterpreter(
//    FIXME wut ? customiseDocsModel = _.servers(List(Server("/v1", None))),
//    FIXME affecst? redocUIOptions = RedocUIOptions.default.pathPrefix(List("v1", "docs"))
  ).fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints
}

object Library {
  case class AZ(name: String)
  case class BZ(name: String)
  case class CZ(name: String)
  case class DZ(name: String)
  case class Book(
      title: String,
      @Schema.annotations.deprecated year: Int,
      a1: AZ,
      a2: Option[AZ],
      @Schema.annotations.deprecated b1: BZ,
      b2: Option[BZ],
      c1: CZ,
      @Schema.annotations.deprecated c2: Option[CZ],
      @Schema.annotations.deprecated d1: DZ,
      @Schema.annotations.deprecated d2: Option[DZ]
  )

  val books = List(
    Book(
      "A The Sorrows of Young Werther",
      1774,
      AZ("A a1"),
      Some(AZ("A a2")),
      BZ("A b1"),
      Some(BZ("A b2")),
      CZ("A c1"),
      Some(CZ("A c2")),
      DZ("A d1"),
      Some(DZ("A d2"))
    ),
    Book(
      "B On the Niemen",
      1888,
      AZ("B a1"),
      Some(AZ("B a2")),
      BZ("B b1"),
      Some(BZ("B b2")),
      CZ("B c1"),
      Some(CZ("B c2")),
      DZ("B d1"),
      Some(DZ("B d2"))
    ),
    Book(
      "C The Art of Computer Programming",
      1968,
      AZ("C a1"),
      Some(AZ("C a2")),
      BZ("C b1"),
      Some(BZ("C b2")),
      CZ("C c1"),
      Some(CZ("C c2")),
      DZ("C d1"),
      Some(DZ("C d2"))
    )
  )
}
