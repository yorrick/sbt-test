package com.yorrick

/**
 * @author ${user.name}
 */
object OtherApp {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Other Hello World!" )
    println("concat arguments = " + foo(args))
  }

}
