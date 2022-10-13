ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    name := "httpclient",

    libraryDependencies ++= Seq(
      "com.github.alexarchambault" %% "case-app" % "2.1.0-M18",
      "org.http4s" %%% "http4s-curl" % "0.1.1",
    ),

    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
