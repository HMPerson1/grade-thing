lazy val commonSettings = Seq(
//  organization := "com.example",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "grade-thing",
    fork := true,
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-math3" % "3.2",
      "org.scalafx" %% "scalafx" % "8.0.60-R9",
      "com.lynden" % "GMapsFX" % "1.1.1",
      "com.github.tototoshi" %% "scala-csv" % "1.2.2")
  )

