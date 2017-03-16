package com.telstra.daas.ai.harold.masker.mask

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.telstra.daas.ai.harold.masker.{JsonSupport, Request, Result}
import io.swagger.annotations._
import spray.json._

import scala.concurrent.ExecutionContext

/**
  * Created by markmo on 12/03/2017.
  */
@Api(value = "/mask", produces = "application/json")
@Path("/mask")
class MaskService(nameDetectorActor: ActorRef,
                  emailDetectorActor: ActorRef,
                  phoneDetectorActor: ActorRef,
                  dobDetectorActor: ActorRef,
                  log: LoggingAdapter
                 )(implicit executionContext: ExecutionContext)
  extends JsonSupport {

  import StatusCodes._
  import akka.pattern.ask

  import scala.concurrent.duration._

  implicit val timeout = Timeout(20.seconds)

  val routes = mask

  @ApiOperation(httpMethod = "POST", response = classOf[Result], value = "Returns a masking result")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "text message",
      required = true,
      dataType = "com.telstra.daas.ai.harold.masker.Request",
      paramType = "body"
    )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return result", response = classOf[Result]),
    new ApiResponse(code = 404, message = "Empty text in request")
  ))
  def mask =
    path("mask") {
      post {
        entity(as[Request]) { req =>
          log.debug("received body:\n{}", req.toJson.prettyPrint)
          val text = req.text
          if (text.isEmpty) {
            complete(HttpResponse(NotFound))
          } else {
            val detectNames = nameDetectorActor ? req
            val detectEmails = emailDetectorActor ? req
            val detectPhoneNumbers = phoneDetectorActor ? req
            val detectDatesOfBirth = dobDetectorActor ? req
            val f = for {
              nameResult <- detectNames.mapTo[Result]
              emailResult <- detectEmails.mapTo[Result]
              phoneResult <- detectPhoneNumbers.mapTo[Result]
              dobResult <- detectDatesOfBirth.mapTo[Result]
            } yield {
              Array(nameResult, emailResult, phoneResult, dobResult).forall(_.pass)
            }
            complete {
              f.map { pass =>
                val result = Result(text, pass)
                HttpEntity(ContentTypes.`application/json`, result.toJson.compactPrint)
              }
            }
          }
        }
      }
    }

}
