package be.coiba.fixedlength

import cats.effect.Sync
import cats.syntax.functor._
import fs2._

object StreamParser {
  def parseRecord[F[_]: Sync](s: Stream[F, String], definition: RecordDefinition): F[Record] =
    s.mapAccumulate(definition.fields -> "") {
        case ((fieldDef :: fieldDefs, acc), in) if fieldDef.length <= acc.length + in.length =>
          val (stringWithLength, rest) = (acc + in).splitAt(fieldDef.length)
          (fieldDefs, rest) -> Some(Field(fieldDef.name, stringWithLength))
        case ((Nil, _), _) => (Nil, "") -> None
      }
      .map(_._2)
      .unNone
      .compile
      .toList
      .map(Record)

}
