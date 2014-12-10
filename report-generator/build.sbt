organization := "gov.tubitak.minder.test"

name := "report-generator"

version := "0.0.1"

resolvers += Resolver.mavenLocal

crossPaths := false

javacOptions in (Compile, compile) ++= Seq("-source", "1.7", "-target", "1.7")

javacOptions in (doc) ++= Seq("-source", "1.7")

libraryDependencies ++= Seq(
  "gov.tubitak.minder" % "minder-common" % "0.0.3"
)
