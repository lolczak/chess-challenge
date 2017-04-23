package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz._

trait CliParser {

  def parse(args: List[String]): ValidationNel[ParseFailure, ChessConfig]

}
