package be.coiba.fixedlength

import cats._
import cats.implicits._
import cats.effect._
import fs2._
import org.scalatest.{FlatSpec, Matchers}

class StreamParserSpec extends FlatSpec with Matchers {

  import StreamParserSpec._

  "StreamParser" should "parse all fields from a Stream of one character elements" in {
    val input = Stream("a", "b", "c", "d", "e", "f")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe record
  }

  it should "parse all fields from a Stream of one long element" in {
    val input = Stream("abcdef")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe record
  }

  it should "parse all fields from a Stream of one long element (longer than expected)" in {
    val input = Stream("abcdefghi")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe record
  }

  it should "parse all fields from a Stream with field values extending over multiple elements" in {
    val input = Stream("abc", "d", "ef")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe record
  }

  it should "parse all fields from a Stream that's shorter than expected and leave the missing fields empty" in {
    val input = Stream("ab")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe Record(
      List(
        Field("A", "ab"),
        Field("B", ""),
        Field("C", "")
      )
    )
  }

  it should "parse all fields from a Stream that stops midway a field" in {
    val input = Stream("abc")
    StreamParser.parseRecord[IO](input, definition).unsafeRunSync() shouldBe Record(
      List(
        Field("A", "ab"),
        Field("B", "c"),
        Field("C", "")
      )
    )
  }
}

object StreamParserSpec {
  val definition = RecordDefinition(
      List(
        FieldDefinition("A", 2),
        FieldDefinition("B", 3),
        FieldDefinition("C", 1)
      )
    )

  val record = Record(
      List(
        Field("A", "ab"),
        Field("B", "cde"),
        Field("C", "f")
      )
    )
}