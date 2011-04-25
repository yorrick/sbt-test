package com.yorrick

import org.scalatest._

class UtilsTest extends Suite {

	def testGetParam() {
		assert(Utils.getParam === "PARAM TOTO")
	}
	
}