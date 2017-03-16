package com.telstra.daas.ai.harold.masker

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import com.telstra.daas.ai.harold.masker.detectors.{DobDetectorActor, EmailDetectorActor, NameDetectorActor, PhoneDetectorActor}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Core is type containing the ``system: ActorSystem`` member. This enables us
  * to use it in our apps as well as in our tests.
  *
  * Created by markmo on 12/03/2017.
  */
trait Core {

  implicit def system: ActorSystem

  lazy val log = Logging.getLogger(system, this)

  val config = ConfigFactory.load()

}

trait BootedCore extends Core {

  // Construct the ActorSystem we will use in our application
  implicit lazy val system = ActorSystem("masker")

  // Ensure that the constructed ActorSystem is shut down when the JVM shuts down
  sys.addShutdownHook {
    log.info("Terminating Actor System...")
    system.terminate()
    Await.result(system.whenTerminated, 30.seconds)
    log.info("Terminated. Bye!")
  }

}

/**
  * This trait contains the actors that make up our application; it can be
  * mixed in with ``BootedCore`` for running code or ``TestKit`` for unit and
  * integration tests.
  */
trait CoreActors {
  this: Core =>

  val nameDetector = system.actorOf(Props[NameDetectorActor])
  val emailDetector = system.actorOf(Props[EmailDetectorActor])
  val phoneDetector = system.actorOf(Props[PhoneDetectorActor])
  val dobDetector = system.actorOf(Props[DobDetectorActor])

}
