package be.coiba.fixedlength

import cats.effect._
import cats._
import cats.implicits._
import fs2.{Stream, io, text}
import java.nio.file.{Path, Paths}

final case class FieldDefinition(name: String, length: Int)

final case class RecordDefinition(fields: List[FieldDefinition]) {
  def length: Int = fields.map(_.length).sum
}

object RecordDefinition {
  def fromPath[F[_]: ContextShift: Functor: Sync](path: Path): F[RecordDefinition] =
    linesReader(path)
      .flatMap(_.split(",") match {
        case Array(fieldName, length) =>
          Stream.eval(Sync[F].delay(FieldDefinition(fieldName, Integer.parseInt(length))))
        case _ =>
          Stream.empty
      })
      .compile
      .toList
      .map(RecordDefinition(_))

  private def linesReader[F[_]: ContextShift: Sync](path: Path): Stream[F, String] =
    FileIO
      .readFile(path)
      .through(text.lines)
      .map(_.trim)
      .filterNot(_.startsWith("#"))

}
