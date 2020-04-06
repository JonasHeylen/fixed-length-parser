package be.coiba.fixedlength

import cats.effect._
import cats.implicits._
import fs2._

object StreamParser {
  def parseRecord[F[_]: Sync](s: Stream[F, String], definition: RecordDefinition): F[Record] =
    s.through(parseFields(definition))
      .compile
      .toList
      .map(Record(_))

  def parseFields[F[_]](definition: RecordDefinition): Pipe[F, String, Field] = {
    def go(
        in: Stream[F, String],
        currentFieldAcc: String,
        fields: List[FieldDefinition]
    ): Pull[F, Field, Unit] =
      if (fields.isEmpty)
        Pull.done
      else {
        val currentField = fields.head
        in.pull.uncons1.flatMap {
          case Some((hd, tl)) =>
            val newAcc = currentFieldAcc + hd
            if (newAcc.length < currentField.length)
              go(tl, newAcc, fields)
            else {
              val (fieldValue, remainderOfChunk) = newAcc.splitAt(currentField.length)
              Pull.output(Chunk(Field(currentField.name, fieldValue))) >>
                go(Stream.chunk(Chunk(remainderOfChunk)) ++ tl, "", fields.tail)
            }
          case None => Pull.output(Chunk(Field(currentField.name, currentFieldAcc))) >> go(Stream.empty, "", fields.tail)
        }
      }
    in => go(in, "", definition.fields).stream
  }
}
