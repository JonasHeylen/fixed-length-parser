package be.coiba.fixedlength

import cats.effect.Sync
import cats.implicits._
import fs2._

object StreamParser {
  def parseRecord[F[_]: Sync](s: Stream[F, String], definition: RecordDefinition): F[Record] =
    (s.flatMap(str => Stream.emits(str.toCharArray())) ++ Stream.constant(' '))
      .take(definition.length)
      .mapAccumulate(definition.fields -> "") {
        case ((fieldDef :: fieldDefs, acc), in) if fieldDef.length <= acc.length + 1 =>
          val (stringWithLength, rest) = (acc + in).splitAt(fieldDef.length)
          (fieldDefs, rest) -> Some(Field(fieldDef.name, stringWithLength))
        case ((Nil, _), _)          => (Nil, "") -> None
        case ((fieldDefs, acc), in) => (fieldDefs, acc + in) -> None
      }
      .map(_._2)
      .unNone
      .compile
      .toList
      .map(Record)
}
