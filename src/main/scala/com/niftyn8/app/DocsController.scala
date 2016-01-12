package com.niftyn8.app

import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}

class DocsController(implicit val swagger: Swagger) extends UghServlet with NativeSwaggerBase

class IssuesSwagger extends Swagger("1.0", "1", new ApiInfo("Ugh API",
  "Dumping ground service for tracking waste during development cycles!",
  "https://github.com/niftyn8/ugh/README.md",
  "natescott.west@gmail.com",
  "MIT",
  "https://opensource.org/licenses/MIT"))
