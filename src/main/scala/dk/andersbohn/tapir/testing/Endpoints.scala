package dk.andersbohn.tapir.testing

import dk.andersbohn.tapir.testing.Library.Event
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{Task, ZIO}

object Endpoints {

  implicitly[Schema[Event]].asIterable[List]

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
