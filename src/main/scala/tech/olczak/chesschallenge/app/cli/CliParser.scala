package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz.\/

trait CliParser {

  def parse(args: List[String]): ParseFailure \/ ChessConfig

}

case class ParseFailure(msg: String)
