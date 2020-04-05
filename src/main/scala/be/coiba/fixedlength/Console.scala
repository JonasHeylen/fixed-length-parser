package be.coiba.fixedlength

import cats.effect.IO

object Console {
  def putStr(text: String): IO[Unit] = IO(print(text))
  def putStrLn(text: String): IO[Unit] = IO(println(text))
  def printPadded(text: String, width: Int) = putStr(text.padTo(width, ' '))
  val reset = putStr("\u001B[0m")
  val blue = putStr("\u001B[34m")
  val newLine = putStrLn("")
}
