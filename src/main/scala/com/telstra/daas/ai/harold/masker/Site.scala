package com.telstra.daas.ai.harold.masker

import akka.http.scaladsl.server.Directives

/**
  * Created by markmo on 12/03/2017.
  */
trait Site extends Directives {

  val site =
    path("swagger") { getFromResource("swagger/index.html") } ~
      getFromResourceDirectory("swagger")

}