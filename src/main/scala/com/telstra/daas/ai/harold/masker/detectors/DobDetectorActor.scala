package com.telstra.daas.ai.harold.masker.detectors

import java.util.Calendar

import akka.actor.{Actor, ActorLogging}
import com.joestelmach.natty.Parser
import com.telstra.daas.ai.harold.masker.{Request, Result}

import scala.collection.JavaConverters._

/**
  * Created by markmo on 16/03/2017.
  */
class DobDetectorActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case Request(text) =>
      log.debug("detecting dates of birth")
      val cal = Calendar.getInstance()
      cal.add(Calendar.YEAR, -16)
      val cutoff = Calendar.getInstance()
      cutoff.set(cal.get(Calendar.YEAR), 0, 1)
      val parser = new Parser()
      val groups = parser.parse(text)
      val found =
        groups.asScala
          .flatMap(_.getDates.asScala)
          .exists(_.before(cutoff.getTime))

      sender ! Result(text, !found)
  }

}
