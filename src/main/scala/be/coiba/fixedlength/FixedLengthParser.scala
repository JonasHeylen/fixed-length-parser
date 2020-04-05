package be.coiba.fixedlength

import cats.effect._
import cats.implicits._


object FixedLengthParser extends IOApp {

  import Console._

  val testRecord = Record(
    List(
      Field("foo", "01234567890123456789012345"),
      Field("bar", "short field"),
      Field("optional", "")
    )
  )

  val ColumnSpacing = 2
  val Width = 22

  def run(args: List[String]): IO[ExitCode] = {
    testRecord.fields
      .traverse(printField(_, testRecord.maxFieldNameLength + ColumnSpacing, Width))
      .as(ExitCode.Success)
  }

  def printField(
      field: Field,
      nameWidth: Int,
      valueWidth: Int
  ): IO[Unit] = {
    val (firstLine, remainder) = field.value.splitAt(valueWidth)
    blue *> printPadded(field.name, nameWidth) *> reset *>
      printPadded(firstLine, valueWidth) *> newLine *>
      (if (remainder.nonEmpty)
         printField(Field("", remainder), nameWidth, valueWidth)
       else
         IO.unit)
  }
}
