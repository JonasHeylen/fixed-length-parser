import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "be.coiba"
ThisBuild / organizationName := "Coiba"

lazy val root = (project in file("."))
  .settings(
    name := "fixed-lenght-parser",
    libraryDependencies ++= Seq(cats, catsEffect),
    libraryDependencies += scalaTest % Test
  )