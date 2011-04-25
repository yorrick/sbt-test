package com.yorrick

class BackendCalculator(val param : Int) {

	def calculate(a : Int, b : Int) = {
    println("param cx" + Utils.getParam)
    (a + b) / param
  }

}

