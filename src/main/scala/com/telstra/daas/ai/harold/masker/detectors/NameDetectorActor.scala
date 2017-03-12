package com.telstra.daas.ai.harold.masker.detectors

import akka.actor.{Actor, ActorLogging}
import com.telstra.daas.ai.harold.masker.{Request, Result}

import scala.io.Source

/**
  * Created by markmo on 12/03/2017.
  */
class NameDetectorActor extends Actor with ActorLogging {

  val firstNames = Source.fromResource("CSV_Database_of_First_Names.csv").getLines
    .toList.tail.map(_.toLowerCase).toSet

  val lastNames = Source.fromResource("CSV_Database_of_Last_Names.csv").getLines
    .toList.tail.map(_.toLowerCase).toSet

  override def receive: Receive = {
    case Request(text) =>
      log.debug("detecting names")
      val tokens = tokenize(text)
      log.debug("tokens: {}", tokens.mkString(" "))
      val found = tokens.exists(firstNames.contains) || tokens.exists(lastNames.contains)
      log.debug("found: {}", found)
      sender ! Result(text, !found)
  }

}
