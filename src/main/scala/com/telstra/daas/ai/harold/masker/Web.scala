package com.telstra.daas.ai.harold.masker

import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

/**
  * Created by markmo on 12/03/2017.
  */
trait Web extends Site with CorsSupport {
  this: Api with CoreActors with Core =>

  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(corsHandler(routes ~ site), "0.0.0.0", 8080)

}
