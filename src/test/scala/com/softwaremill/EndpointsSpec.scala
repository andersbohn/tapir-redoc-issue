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
        OpenAPIDocsInterpreter().toOpenAPI(List(Endpoints.postEvent), "vague-chipmunk", "1.0.0")
      val yaml = docs.toYaml
      if (sys.env.contains("REGENERATE_DOCS_YAML")) {
        Files.write(java.nio.file.Paths.get("./src/test/resources/docs.yaml"), yaml.getBytes(StandardCharsets.UTF_8))
      }
      val expected = scala.io.Source.fromResource("docs.yaml")(StandardCharsets.UTF_8).mkString
      assertZIO(ZIO.succeed(yaml))(equalTo(expected))
    }
  )
}
