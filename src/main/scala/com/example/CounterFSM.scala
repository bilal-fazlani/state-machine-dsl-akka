package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object CounterFSM {

  trait Message
  case class Number(value: Int) extends Message
  case object Get extends Message

  def incrementing(state:Int): Behavior[Message] =
    Behaviors.receiveMessage {
      case Number(value) =>
        println(s"incrementing state from $state to ${state + value}")
        decrementing(state + value)
      case Get =>
        println(s"current state is $state")
        Behaviors.same
    }

  def decrementing(state:Int): Behavior[Message] =
    Behaviors.receiveMessage {
      case Number(value) =>
        println(s"decrementing state from $state to ${state - value}")
        incrementing(state - value)
      case Get =>
        println(s"current state is $state")
        Behaviors.same
    }
}