import sbt._

class PocketChangeProject(info: ProjectInfo) extends ParentProject(info) with IdeaProject {

	//lazy val hi = task {println("hello world"); None}
	
	//override def compileOptions = super.compileOptions ++ Seq(Verbose)

	// val notTOTOFilter : String => Boolean = {testName : String => println("XXXXXXXXXXXXXXXXXXXXXXXXX"); ! testName.contains("TOTO")}
	// override def testOptions = TestFilter(notTOTOFilter) :: super.testOptions.toList
	
	//val scalaTest = "org.scalatest" % "scalatest" % "1.3"
	
	// override def mainClass : Option[String] = Some("com.yorrick.OtherApp")

  lazy val frontend  = project("frontend", "frontend", info => new LiftProject(info), backend, utils)
  lazy val backend   = project("backend", "backend", info => new BackendProject(info), utils)
  lazy val utils     = project("utils", "utils project", new UtilsProject(_))

  // test dependencies
  val scalatest      = "org.scalatest" % "scalatest" % "1.3" % "test-> default"
  val junit     = "junit" % "junit" % "4.5" % "test->default"
  val specs     = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test->default"

  val liftVersion = "2.3-RC3"
  val webkit    = "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default"
  val logback   = "ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default"
  val wizard = "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default"
  val mapper = "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default"
  val h2 = "com.h2database" % "h2" % "1.2.138"

  val servlet   = "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
  val jetty6    = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"

  override def parallelExecution = true

  val runFrontend = frontend.jettyRun
  val stopFrontend = frontend.jettyStop

  import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin
  import fi.jawsy.sbtplugins.jrebel.JRebelJarPlugin

  trait MavenSourceProject extends DefaultProject {
    override def managedStyle = ManagedStyle.Maven

    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar= defaultJarPath("-sources.jar")

    lazy val sourceArtifact = Artifact.sources(artifactID)
    lazy val docsArtifact = Artifact.javadoc(artifactID)
    override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
  }

  class BackendProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject with JRebelJarPlugin with MavenSourceProject {
    //val dependsOnUtils = utils
    override def libraryDependencies = Set(
      scalatest
    ) ++ super.libraryDependencies
  }

  class UtilsProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject with JRebelJarPlugin with MavenSourceProject {
    override def libraryDependencies = Set(
      scalatest
    ) ++ super.libraryDependencies
  }

  class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) with IdeaProject with JRebelWebPlugin {
    override def libraryDependencies = Set(
      junit, specs, webkit, logback, wizard, mapper, h2, servlet, jetty6
    ) ++ super.libraryDependencies

    /**
     * Maven repositories
     */
    //lazy val scalatoolsSnapshots = ScalaToolsSnapshots

    override def scanDirectories = Nil
  }
}
