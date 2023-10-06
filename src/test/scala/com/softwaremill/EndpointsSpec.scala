package com.softwaremill

import zio.ZIO
import zio.test.Assertion._
import zio.test.{ZIOSpecDefault, assertZIO}

import java.nio.charset.StandardCharsets
import java.nio.file.Files

object EndpointsSpec extends ZIOSpecDefault {
  def spec = suite("Endpoints spec")(
    test("expectd yaml") {
      import sttp.apispec.openapi._
      import sttp.apispec.openapi.circe.yaml._
      import sttp.tapir.docs.openapi._
      val docs: OpenAPI =
        OpenAPIDocsInterpreter().toOpenAPI(List(Endpoints.bookAsListing, Endpoints.bookBsListing), "vague-chipmunk", "1.0.0")
      if (false) {
        Files.write(java.nio.file.Paths.get("gen-docs.yaml"), docs.toYaml.getBytes(StandardCharsets.UTF_8))
      }
      val expected = scala.io.Source.fromResource("docs.yaml").mkString
      assertZIO(ZIO.succeed(docs.toYaml))(equalTo(expected))
    }
  )
}
