package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz.{-\/, \/}

object SimpleCliParser extends CliParser {

  override def parse(args: List[String]): \/[ParseFailure, ChessConfig] = {
    if (args.size < 3) -\/(ParseFailure("Too less arguments"))
    else throw new RuntimeException
  }
}
