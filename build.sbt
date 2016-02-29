name := "piconot-ben"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= 
  Seq( "org.scalatest" %% "scalatest" % "2.2.6" % "test",
       "org.scalacheck" %% "scalacheck" % "1.13.0" % "test",
       "org.scalafx" % "scalafx_2.11" % "8.0.5-R5",
       "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
       "org.scala-lang" % "scala-compiler" % scalaVersion.value )

unmanagedClasspath in (Compile, runMain) += baseDirectory.value / "resources"
