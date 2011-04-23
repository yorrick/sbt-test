package com.yorrick

import org.scalatest.Suite

class UtilsTest extends Suite {

	def testGetParam() {
		assert(Utils.getParam === "PARAM TOTO")
	}
	
}