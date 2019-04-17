package com.example

class StateMachine

trait MachineOp

class State extends MachineOp

trait StateOp

class Entry extends StateOp
class When  extends StateOp
class Exit  extends StateOp

trait WhenOp

class Transition extends WhenOp

class DSL[T] {
  def stateMachine(name: String)(machine: => MachineOp): StateMachine = ???

  def state(name: String)(state : => StateOp): State = ???

  def entry(entryFunction: => Unit): Entry = ???

  def when(eventFunction: T => Boolean)(actionFunction: => Transition): When = ???

  def exit(exitFunction: => Unit): Exit= ???

  def transition(name: String): Transition = ???
}

object Main extends App {
  val dsl = new DSL[Int]
  import dsl._

  stateMachine("machine") {
    state("A"){
      entry {
        println("A state entered")
      }

      when(voltage => voltage > 5) {
        println("voltage is high")
        transition("B")
      }

      when(voltage => voltage <= 5) {
        println("voltage is low")
        transition("C")
      }

      exit {
        println("A state exited")
      }
    }

    state("B"){
      exit {
        println("B state exited")
      }
    }
  }
}


/*
  Statemachine "ABC"{
    Initial State "A"{
        entry{
          entryFunction(){}
        }

        when(eventFunction() /*conditions*/) {
            actionFunction()
            transition "B"
        }
        when(eventFunction() /*conditions*/) {
          actionFunction()
          transition "C"
        }

        exit {
          exitFunction(){}
        }
      }

    State "B"{
      entryFunction(){}

      when(eventFunction() /*conditions*/) {
        actionFunction()
        transition "C"
      }

      exitFunction(){}
      }
  }

*/