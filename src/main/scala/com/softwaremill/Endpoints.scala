package com.softwaremill

import com.softwaremill.Library._
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{Task, ZIO}

object Endpoints {

  val eventsListing: PublicEndpoint[Unit, Unit, List[Event], Any] = endpoint.get
    .in("events" / "list" / "all")
    .out(jsonBody[List[Event]])
  val postEvent = endpoint.post
    .in("events").in(jsonBody[Event]).out(emptyOutput)

  val apiEndpoints: List[ZServerEndpoint[Any, Any]] = List(
    eventsListing.serverLogicSuccess(_ => ZIO.succeed(List.empty[Event])),
    postEvent.serverLogicSuccess(event => ZIO.succeed(()))
  )

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](apiEndpoints, "vague-chipmunk", "1.0.0")

  val all: List[ZServerEndpoint[Any, Any]] = apiEndpoints ++ docEndpoints
}

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName

  sealed trait Event
  case class EventA(
      aName: AName,
      anotherName: AName
  ) extends Event
  case class EventB(
      aName: AName,
      @Schema.annotations.deprecated deprAName: AName,
      bName: BName
  )extends Event
  case class EventC(
      @Schema.annotations.deprecated deprBName: BName,
      bName: BName
  )extends Event
  case class EventD(
      aName: BName,
      bName: BName
  )extends Event

  implicit val aNameSchema: Schema[AName] = Schema.derived[AName]
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName]
  implicit val eventASchema: Schema[EventA] = Schema.derived[EventA]
  implicit val eventBSchema: Schema[EventB] = Schema.derived[EventB].modify(_.deprAName)(_.name(None)).name(None)
  implicit val eventCSchema: Schema[EventC] = Schema.derived[EventC]
  implicit val eventDSchema: Schema[EventD] = Schema.derived[EventD]
  implicit val eventSchema: Schema[Event] = Schema.derived[Event]

}
