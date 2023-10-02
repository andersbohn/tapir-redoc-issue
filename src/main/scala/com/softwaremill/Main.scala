package com.softwaremill

import com.comcast.ip4s.{ Host, IpLiteralSyntax, Port }
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.interop.catz._
import zio.stream.interop.fs2z.io.networkInstance
import zio.{ Console, Scope, Task, ZIO, ZIOAppArgs, ZIOAppDefault }

object Main extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {

    val routes = ZHttp4sServerInterpreter().from(Endpoints.all).toRoutes[Any]

    val port = sys.env
      .get("HTTP_PORT")
      .flatMap(_.toIntOption)
      .flatMap(Port.fromInt)
      .getOrElse(port"8080")

    EmberServerBuilder
      .default[Task]
      .withHost(Host.fromString("localhost").get)
      .withPort(port)
      .withHttpApp(Router("/" -> routes).orNotFound)
      .build
      .use { server =>
        for {
          _ <- Console.printLine(s"Go to http://localhost:${server.address.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
          _ <- Console.readLine
        } yield ()
      }
  }
}
