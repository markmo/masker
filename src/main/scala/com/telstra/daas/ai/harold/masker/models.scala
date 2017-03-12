package com.telstra.daas.ai.harold.masker

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import io.swagger.annotations.{ApiModel, ApiModelProperty}
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.field

/**
  * Created by markmo on 12/03/2017.
  */

@ApiModel(description = "Masker request")
case class Request(
                    @(ApiModelProperty @field)(value = "text message")
                    text: String
                  )

@ApiModel(description = "Masker result")
case class Result(
                   @(ApiModelProperty @field)(value = "original text")
                   text: String,

                   @(ApiModelProperty @field)(value = "pass result")
                   pass: Boolean
                 )

trait JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val requestJsonFormat = jsonFormat1(Request)
  implicit val resultJsonFormat = jsonFormat2(Result)
}