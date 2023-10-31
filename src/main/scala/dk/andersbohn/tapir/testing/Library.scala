package dk.andersbohn.tapir.testing

import sttp.tapir.Schema

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName

  sealed trait EventType
  case object EventTypeA extends EventType
  case object EventTypeB extends EventType

  sealed trait EventParameters
  case class EventParametersA(s: String, aName: AName) extends EventParameters
// TODO introducing another subtype for EventParameters leads to a tapir NPE - like just adding EventParametersB => boom
//  case class EventParametersB(deprBName: BName) extends EventParameters
  case class EventParametersB(@Schema.annotations.deprecated deprBName: BName) extends EventParameters
  case class EventParametersC(deprBName: BName) extends EventParameters

  sealed trait Event {
    def eventParameters: EventParameters
  }
  case class EventA(
      aName: AName,
      anotherName: AName,
      eventParameters: EventParametersA,
      eventType: EventType
  ) extends Event
  case class EventB(
      aName: AName,
      @Schema.annotations.deprecated
      deprAName: AName,
      bName: BName,
      eventParameters: EventParametersB
  ) extends Event
  case class EventC(
      @Schema.annotations.deprecated
      deprBName: BName,
      bName: BName,
      eventParameters: EventParametersC
  ) extends Event
  case class EventD(
      aName: BName,
      bName: BName,
      eventParameters: EventParametersA
  ) extends Event

  implicit val aNameSchema: Schema[AName] = Schema.derived[AName]
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName]
  implicit val EventTypeASchema: Schema[EventTypeA.type] = Schema.derived[EventTypeA.type]
  implicit val EventTypeBSchema: Schema[EventTypeB.type] = Schema.derived[EventTypeB.type]
  implicit val eventTypeSchema: Schema[EventType] = Schema.derived[EventType]
  implicit val eventTypeParametersASchema: Schema[EventParametersA] = Schema.derived[EventParametersA]
  implicit val eventTypeParametersBSchema: Schema[EventParametersB] = Schema.derived[EventParametersB]
  implicit val eventTypeParametersSchema: Schema[EventParameters] = Schema.derived[EventParameters]
  implicit val ccNameSchema: Schema[CcName] = Schema.derived[CcName]
  implicit val eventTypeParametersCSchema: Schema[EventParametersC] = Schema.derived[EventParametersC]
  implicit val eventASchema: Schema[EventA] = Schema.derived[EventA]
  implicit val eventBSchema: Schema[EventB] = Schema.derived[EventB]
  implicit val eventCSchema: Schema[EventC] = Schema.derived[EventC]
  implicit val eventDSchema: Schema[EventD] = Schema.derived[EventD]
  implicit val eventSchema: Schema[Event] = Schema.derived[Event]

}
