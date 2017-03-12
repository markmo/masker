package com.telstra.daas.ai.harold.masker.detectors

import akka.actor.{Actor, ActorLogging}
import com.telstra.daas.ai.harold.masker.{Request, Result}

/**
  * Created by markmo on 12/03/2017.
  */
class EmailDetectorActor extends Actor with ActorLogging {

  val re = """(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".r

  override def receive: Receive = {
    case Request(text) =>
      log.debug("detecting emails")
      val found = re.findFirstIn(text).isDefined
      log.debug("found: {}", found)
      sender ! Result(text, !found)
  }

}
