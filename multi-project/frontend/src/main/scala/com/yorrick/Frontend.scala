package com.yorrick

object Frontend {

  val calculator = new BackendCalculator(1)

  def main(args : Array[String]) {
    println("doIt " + calculator.calculate(2, 3))
  }

}


