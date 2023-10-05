package com.softwaremill

import sttp.tapir._
import Library._
import io.circe.generic.auto._
import sttp.tapir.Schema.annotations.encodedName
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.redoc.RedocUIOptions
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
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
  val booksListingServerEndpoint: ZServerEndpoint[Any, Any] = booksListing.serverLogicSuccess(_ => ZIO.succeed(List.empty[Book]))

  val apiEndpoints: List[ZServerEndpoint[Any, Any]] = List(helloServerEndpoint, booksListingServerEndpoint)

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

//  val docEndpoints: List[ZServerEndpoint[Any, Any]] = RedocInterpreter().fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints
}

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(i: Int) extends CcName
  case class Book(
      title: String,
      year: Int,
      aName: AName,
      @Schema.annotations.deprecated
      deprAName: AName,
      bName: BName,
      cName: CcName,
      dName: CcName
  )

}
