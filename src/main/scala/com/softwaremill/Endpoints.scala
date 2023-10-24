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
    .in("events")
    .in(jsonBody[Event])
    .out(emptyOutput)

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

  sealed trait EventType
  case object EventTypeA extends EventType
  case object EventTypeB extends EventType

  sealed trait EventParameters
//  case class EventParametersA(s: String) extends EventParameters
  case class EventParametersB(@Schema.annotations.deprecated deprBName: BName) extends EventParameters
//  case class EventParametersC(deprBName: BName) extends EventParameters

//  case class CreateEvent(eventType: EventType, eventToCreate: Event)

  sealed trait Event // {
  // def eventParameters: EventParameters
  // }
  case class EventA(
      aName: AName,
      anotherName: AName
//      eventParameters: EventParametersA
  ) extends Event
  case class EventB(
      aName: AName,
//      @Schema.annotations.deprecated
      deprAName: AName,
      bName: BName
//      eventParameters: EventParametersB
  ) extends Event
  case class EventC(
//      @Schema.annotations.deprecated
      deprBName: BName,
      bName: BName
//      eventParameters: EventParametersA
  ) extends Event
  case class EventD(
      aName: BName,
      bName: BName
//      eventParameters: EventParametersA
  ) extends Event

//  implicit val eventTypeParametersASchema: Schema[EventParametersA] = Schema.derived[EventParametersA]
  implicit val eventTypeParametersBSchema: Schema[EventParametersB] = Schema.derived[EventParametersB]
//  implicit val eventTypeParametersCSchema: Schema[EventParametersC] = Schema.derived[EventParametersC]
  implicit val eventTypeSchema: Schema[EventType] = Schema.derived[EventType]
  implicit val aNameSchema: Schema[AName] = Schema.derived[AName]
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName]
  implicit val eventASchema: Schema[EventA] = Schema.derived[EventA]
  implicit val eventBSchema: Schema[EventB] = Schema.derived[EventB] // TODO wut? .modify(_.deprAName)(_.name(None)).name(None)
  implicit val eventCSchema: Schema[EventC] = Schema.derived[EventC]
  implicit val eventDSchema: Schema[EventD] = Schema.derived[EventD]
  implicit val eventSchema: Schema[Event] = Schema.derived[Event]
//  implicit val createEventSchema: Schema[CreateEvent] = Schema.derived[CreateEvent]

}
