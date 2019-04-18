package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.Source
import akka.stream.typed.scaladsl.ActorMaterializer

import scala.concurrent.duration.DurationDouble

object VoltageFSM {
  trait Message

  // Can be abstracted as `int v`. It will auto generate this case class!
  case class Voltage(value: Int) extends Message
  private case object SwitchToLow extends Message

  def init : Behavior[Message] = Behaviors.setup { ctx =>
    println("scheduling SwitchToLow after 5 seconds")
    ctx.scheduleOnce(5.seconds, ctx.self, SwitchToLow)

    // this block has to be taken out. For this example, it is showing PV simulation logic.
    // Nothing to do with init state/behaviour
    implicit val mat: ActorMaterializer = ActorMaterializer()(ctx.system)

    Source
      .tick(0.seconds, 1.seconds, ())
      .statefulMapConcat(() => {
        var previousValue = 0
        _ => {
          previousValue match {
            case x if x < 9 =>
              previousValue += 1
              List(previousValue)
            case _ =>
              previousValue = 0
              List(previousValue)
          }
        }
      })
      .runForeach(number => ctx.self ! Voltage(number))

    //I am not sure why we need "switchToLow" here. As any message after 5 secs will transit init -> low
    Behaviors.receiveMessagePartial {
      case SwitchToLow =>
        println("SwitchToLow message received after 5 seconds")
        low(Voltage(0))
    }
  }

  def low(state: Voltage): Behavior[Message] = Behaviors.receiveMessage {
    case Voltage(value) if value <= 5 =>
      println(s"LOW: value = $value")
      Behavior.same
    case Voltage(value) if value > 5 =>
      println(s"new value is $value, hence changing to HIGH")
      high(state)
  }

  def high(state: Voltage): Behavior[Message] = Behaviors.receiveMessage {
    case Voltage(value) if value > 5 =>
      println(s"HIGH: value = $value")
      Behavior.same
    case Voltage(value) if value <= 5 =>
      println(s"new value is $value, hence changing to LOW")
      low(state)
  }
}


/*
Init : 0 for 5 second -> delay
High : > 5
Low : <= 5
----
*/