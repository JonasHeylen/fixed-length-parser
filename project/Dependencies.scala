import sbt._

object Versions {
  val scalaTest = "3.1.1"
  val cats = "2.0.0"
  val catsEffect = "2.1.2"
  val fs2 = "2.2.1"
}

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  val cats = "org.typelevel" %% "cats-core" % Versions.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  val fs2 = "co.fs2" %% "fs2-core" % Versions.fs2
  val fs2Io = "co.fs2" %% "fs2-io" % Versions.fs2
}
