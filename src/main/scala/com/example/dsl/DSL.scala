package com.example.dsl

trait MachineOp

class State(name: String) extends MachineOp

trait StateOp

class Entry extends StateOp
class When  extends StateOp
class Exit  extends StateOp

trait WhenOp

class Transition extends WhenOp

case class StateMachine(states: List[State])

class DSL[T] {
  def stateMachine(name: String)(machine: => State): StateMachine = {
    machine
    ???
  }

  def state(name: String)(state : => StateOp): State = {
    new State(name)
  }

  def entry(entryFunction: => Unit): Entry = ???

  def when(eventFunction: T => Boolean)(actionFunction: => Transition): When = ???

  def exit(exitFunction: => Unit): Exit= ???

  def transition(name: String): Transition = ???
}

