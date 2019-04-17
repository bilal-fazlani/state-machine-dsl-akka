package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

object Main extends App {
  implicit val timeout: Timeout = Timeout(2.seconds)

  implicit val system = ActorSystem(Behaviors.empty, "example")

  val actorRef = Await.result(system.systemActorOf(VoltageFSM.init,"example"), 2.seconds)

//  system.terminate()
}