import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.4.0"

  val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
  val jrebelPlugin = "fi.jawsy" % "sbt-jrebel-plugin" % "0.2.1"

  lazy val cloudbees = "eu.getintheloop" % "sbt-cloudbees-plugin" % "0.2.7"
  lazy val sonatypeRepo = "sonatype.repo" at "https://oss.sonatype.org/content/groups/public"
}


