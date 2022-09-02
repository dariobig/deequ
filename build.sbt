
ThisBuild / organization := "com.amazon.deequ"
ThisBuild / version := "2.0.0-spark-3.2"
ThisBuild / scalaVersion := "2.13.8"
lazy val myProps = new {
  val mavenCompilerSource = "1.8"
  val mavenCompilerTarget = "1.8"
  val encoding = "UTF-8"
  val scalaMajorVersion = "2.12"
  val scalaVersion = s"${myProps.scalaMajorVersion}.10"
  val artifactScalaVersion = myProps.scalaMajorVersion
  val scalaMavenPluginVersion = "4.4.0"
  val sparkVersion = "3.2.1"
}

lazy val root = (project in file("."))
  .settings(
    name := "deequ",
    libraryDependencies ++= List(
      "org.scala-lang" % "scala-library" % myProps.scalaVersion,
      "org.scala-lang" % "scala-reflect" % myProps.scalaVersion,
      "org.apache.spark" % s"spark-core_${myProps.scalaMajorVersion}" % myProps.sparkVersion,
      "org.apache.spark" % s"spark-sql_${myProps.scalaMajorVersion}" % myProps.sparkVersion,
      "org.scalanlp" % s"breeze_${myProps.scalaMajorVersion}" % "0.13.2",
      "org.scalatest" % s"scalatest_${myProps.scalaMajorVersion}" % "3.1.2" % Test,
      "org.scalamock" % s"scalamock_${myProps.scalaMajorVersion}" % "4.4.0" % Test,
      "org.scala-lang" % "scala-compiler" % myProps.scalaVersion % Test,
      "org.mockito" % "mockito-core" % "2.28.2" % Test,
      "org.openjdk.jmh" % "jmh-core" % "1.23" % Test,
      "org.openjdk.jmh" % "jmh-generator-annprocess" % "1.23" % Test,
      "org.apache.datasketches" % "datasketches-java" % "1.3.0-incubating" % Test
    )
  )

lazy val libs = new {

}
