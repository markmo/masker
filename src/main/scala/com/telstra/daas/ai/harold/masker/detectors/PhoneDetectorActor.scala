package com.telstra.daas.ai.harold.masker.detectors

import akka.actor.{Actor, ActorLogging}
import com.telstra.daas.ai.harold.masker.{Request, Result}

/**
  * Created by markmo on 12/03/2017.
  */
class PhoneDetectorActor extends Actor with ActorLogging {

  val re = """\({0,1}((0|\+61)(2|4|3|7|8)){0,1}\){0,1}(\ |-){0,1}[0-9]{2}(\ |-){0,1}[0-9]{2}(\ |-){0,1}[0-9]{1}(\ |-){0,1}[0-9]{3}""".r

  override def receive: Receive = {
    case Request(text) =>
      log.debug("detecting phone numbers")
      val found = re.findFirstIn(text).isDefined
      log.debug("found: {}", found)
      sender ! Result(text, !found)
  }

}
