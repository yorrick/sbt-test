package com.yorrick

import org.scalatest.Suite

class CalculatorTest extends Suite {

	def testOneCalculator() {
		val calc = new Calculator(1)
		val result = calc.calculate(2, 3)
		
		assert(result === 5)
	}
	
	def testOtherTest() {
		val i = 1
		assert(i === 1)
	}
	
	def testPoloTOTO {
		assert(true)
	}
	
}