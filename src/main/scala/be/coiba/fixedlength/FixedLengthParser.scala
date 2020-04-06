package be.coiba.fixedlength

import cats.effect._
import cats.implicits._
import fs2._
import java.nio.file.Paths
import java.nio.file.Path

object FixedLengthParser extends IOApp {

  import Console._

  val Spacing = 2
  val Width = 160

  def run(args: List[String]): IO[ExitCode] =
    args match {
      case List(definitionPath, recordPath) =>
        for {
          recordDefinition <- RecordDefinition.fromPath(Paths.get(definitionPath))
          recordFileStream = FileIO.readFile(Paths.get(recordPath))
          record <- StreamParser.parseRecord(recordFileStream, recordDefinition)
          _ <- record.fields.traverse(printField(_, record.maxFieldNameLength + Spacing, Width))
        } yield ExitCode.Success
      case _ =>
        putStrLn("Usage: FixedLengthParser [recordDefinitionFile] [recordFile]").as(ExitCode.Error)
    }

  def printField(
      field: Field,
      nameWidth: Int,
      valueWidth: Int
  ): IO[Unit] = {
    val (firstLine, remainder) = field.value.splitAt(valueWidth)
    blue *> printPadded(field.name, nameWidth) *> reset *>
      printPadded(firstLine.replace("\n", "\n".padTo(nameWidth + 1, ' ')), valueWidth) *> newLine *>
      (if (remainder.nonEmpty)
         printField(Field("", remainder), nameWidth, valueWidth)
       else
         IO.unit)
  }

}
