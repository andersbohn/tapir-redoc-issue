package dk.andersbohn.tapir.testing

import sttp.tapir.Schema

object Library {

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName

  sealed trait Event
  case class EventA(
      aName: AName,
      anotherName: AName,
      optionAName: Option[AName]
  ) extends Event
  case class EventB(
      aName: AName,
      @Schema.annotations.deprecated
      deprAName: AName,
      @Schema.annotations.deprecated
      deprOptionAName: Option[AName],
      bName: BName
  ) extends Event
  case class EventC(
      @Schema.annotations.deprecated
      deprBName: BName,
      bName: BName
  ) extends Event

  implicit val aNameSchema: Schema[AName] = Schema.derived[AName]
  implicit val bNameSchema: Schema[BName] = Schema.derived[BName]
  implicit val ccNameSchema: Schema[CcName] = Schema.derived[CcName]
  implicit val eventASchema: Schema[EventA] = Schema.derived[EventA]
  implicit val eventBSchema: Schema[EventB] = Schema.derived[EventB]
  implicit val eventCSchema: Schema[EventC] = Schema.derived[EventC]
  implicit val eventSchema: Schema[Event] = Schema.derived[Event]

}
