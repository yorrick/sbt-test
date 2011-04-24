package com.yorrick

object Frontend {

  val calculator = new BackendCalculator(1)

  def main(args : Array[String]) {
    println("doIt " + calculator.calculate(2, 3))

    for( ln <- io.Source.stdin.getLines ) {
      ln match {
        case "exit" => println("exiting"); exit(0)
        case Int(x) => println("calculation " + calculator.calculate(9, x))
        case _ => println("""Please enter a number or type "exit" """)
      }

    }

  }

  object Int {
    def unapply(s : String) : Option[Int] = try {
      Some(s.toInt)
    } catch {
      case _ : java.lang.NumberFormatException => None
    }
  }


}


