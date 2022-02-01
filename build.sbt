val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scraper_scala",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.10",
      "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      "org.jsoup" % "jsoup" % "1.14.3",
      "com.lihaoyi" %% "os-lib" % "0.8.0"
    )
  )
