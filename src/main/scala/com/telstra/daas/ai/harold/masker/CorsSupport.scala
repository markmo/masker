package com.telstra.daas.ai.harold.masker

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}

/**
  * Created by markmo on 4/12/2016.
  */
trait CorsSupport {

  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders(): Directive0 = {
    respondWithHeaders(
      //`Access-Control-Allow-Origin`(HttpOrigin("http", Host("aiplatform.host"))),
      `Access-Control-Allow-Origin`(HttpOrigin("http", Host("localhost", 3000))),
      `Access-Control-Allow-Credentials`(true),
      `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With", "X-IBM-Client-Id", "X-IBM-Client-Secret", "X-Request-ID")
    )
  }

  //this handles preFlight OPTIONS requests
  private def preFlightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  def corsHandler(r: Route) = addAccessControlHeaders() {
    preFlightRequestHandler ~ r
  }

}
