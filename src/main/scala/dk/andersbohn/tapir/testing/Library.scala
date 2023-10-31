package dk.andersbohn.tapir.testing

import sttp.tapir.Schema

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName

//  sealed trait EventType
//  case object EventTypeA extends EventType
//  case object EventTypeB extends EventType

  sealed trait EventParameters
  case class EventParametersA(s: String) extends EventParameters
  // case class EventParametersB(deprBName: BName) extends EventParameters
  //  case class EventParametersB(@Schema.annotations.deprecated deprBName: BName) extends EventParameters
  //  case class EventParametersC(deprBName: BName) extends EventParameters

  //  case class CreateEvent(eventType: EventType, eventToCreate: Event)

  sealed trait Event {
    def eventParameters: EventParameters
  }
  case class EventA(
      aName: AName,
      anotherName: AName,
      eventParameters: EventParametersA
  ) extends Event
  case class EventB(
      aName: AName,
      //      @Schema.annotations.deprecated
      deprAName: AName,
      bName: BName,
      eventParameters: EventParametersA
  ) extends Event
  case class EventC(
      //      @Schema.annotations.deprecated
      deprBName: BName,
      bName: BName,
      eventParameters: EventParametersA
  ) extends Event
  case class EventD(
      aName: BName,
      bName: BName,
      eventParameters: EventParametersA
  ) extends Event

//  implicit val eventTypeParametersSchema: Schema[EventParameters] = Schema.derived[EventParameters]
  implicit val eventTypeParametersASchema: Schema[EventParametersA] = Schema.derived[EventParametersA]
  //  implicit val eventTypeParametersBSchema: Schema[EventParametcleanersB] = Schema.derived[EventParametersB]
  //  implicit val eventTypeParametersCSchema: Schema[EventParametersC] = Schema.derived[EventParametersC]
//  implicit val eventTypeSchema: Schema[EventType] = Schema.derived[EventType]
  implicit val aNameSchema: Schema[AName] = Schema.derived[AName]
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName]
  implicit val eventASchema: Schema[EventA] = Schema.derived[EventA]
  implicit val eventBSchema: Schema[EventB] = Schema.derived[EventB] // TODO wut? .modify(_.deprAName)(_.name(None)).name(None)
  implicit val eventCSchema: Schema[EventC] = Schema.derived[EventC]
  implicit val eventDSchema: Schema[EventD] = Schema.derived[EventD]
  implicit val eventSchema: Schema[Event] = Schema.derived[Event]
  //  implicit val createEventSchema: Schema[CreateEvent] = Schema.derived[CreateEvent]

}
