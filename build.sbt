ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "httpclient",

    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "mainargs" % "0.3.0",
      "org.http4s" %%% "http4s-curl" % "0.1.1",
    )
  )
