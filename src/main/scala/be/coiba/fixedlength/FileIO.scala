package be.coiba.fixedlength

import java.nio.file.Path

import cats.effect._
import fs2.{Stream, io, text}

object FileIO {
  def readFile[F[_]: ContextShift: Sync](path: Path): Stream[F, String] =
    Stream.resource(Blocker[F]).flatMap { blocker =>
      io.file
        .readAll[F](
          path,
          blocker,
          chunkSize = 4096
        )
        .through(text.utf8Decode)
    }
}
