package com.softwaremill

import com.softwaremill.Library._
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{Task, ZIO}

object Endpoints {

  val bookAsListing: PublicEndpoint[Unit, Unit, List[BookA], Any] = endpoint.get
    .in("bookAs" / "list" / "all")
    .out(jsonBody[List[BookA]])
  val bookBsListing: PublicEndpoint[Unit, Unit, List[BookB], Any] = endpoint.get
    .in("bookBs" / "list" / "all")
    .out(jsonBody[List[BookB]])
  val apiEndpoints: List[ZServerEndpoint[Any, Any]] = List(
    bookAsListing.serverLogicSuccess(_ => ZIO.succeed(List.empty[BookA])),
    bookBsListing.serverLogicSuccess(_ => ZIO.succeed(List.empty[BookB]))
  )

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints
}

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName
  case class BookA(
      aName: AName,
      @Schema.annotations.deprecated deprAName: AName
  )
  case class BookB(
      @Schema.annotations.deprecated deprBName: BName,
      bName: BName
  )

  implicit val aNameSchema: Schema[AName] = Schema.derived[AName].name(None)
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName].name(None)
  implicit val bookASchema: Schema[BookA] = Schema.derived[BookA]
  implicit val bookBSchema: Schema[BookB] = Schema.derived[BookB]

}
