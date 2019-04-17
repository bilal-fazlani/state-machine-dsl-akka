package com.example.dsl

object Example {
  val dsl = new DSL[Int]
  import dsl._

  val sm = stateMachine("machine") {
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
    },

    state("B"){
      exit {
        println("B state exited")
      }
    }
  }
}
