import sbt._

class ContinuousTestingProject(info: ProjectInfo) extends DefaultProject(info) {

	lazy val hi = task {println("hello world"); None}
	
	//override def compileOptions = super.compileOptions ++ Seq(Verbose)

	val notTOTOFilter : String => Boolean = {testName : String => println("XXXXXXXXXXXXXXXXXXXXXXXXX"); ! testName.contains("TOTO")} 
	override def testOptions = TestFilter(notTOTOFilter) :: super.testOptions.toList
	
	//val scalaTest = "org.scalatest" % "scalatest" % "1.3"
	
	override def mainClass : Option[String] = Some("com.yorrick.OtherApp")
}