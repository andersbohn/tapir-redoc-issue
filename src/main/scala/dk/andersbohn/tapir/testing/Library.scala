package dk.andersbohn.tapir.testing

import sttp.tapir.Schema

object Library {

//  sealed trait Address
//  case class PostAddress(s: String) extends Address
//  case class EmailAddress(s: String) extends Address
//  case class BlockhainAddress(: String) extends Address

  sealed trait CcName
  case class AName(s: String) extends CcName
  case class BName(s: String) extends CcName

  sealed trait EventType
  case object EventTypeA extends EventType
  case object EventTypeB extends EventType

  sealed trait EventParameters {
    def ccName: CcName
  }
  case class EventParametersA(s: String, aName: AName) extends EventParameters {
    override def ccName: CcName = aName
  }
  case class EventParametersB(@Schema.annotations.deprecated deprBName: BName) extends EventParameters {
    override def ccName: CcName = deprBName
  }
  case class EventParametersC(@Schema.annotations.deprecated deprBName: BName, @Schema.annotations.deprecated optCcName: Option[AName])
      extends EventParameters {
    override def ccName: CcName = optCcName.getOrElse(deprBName)
  }

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
