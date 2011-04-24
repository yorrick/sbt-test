package com.yorrick

import org.scalatest.Suite

class BackendCalculatorTest extends Suite {

	def testOneCalculator() {
		val calc = new BackendCalculator(1)
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