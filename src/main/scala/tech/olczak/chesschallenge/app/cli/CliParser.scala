package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.app.ChessProblem

import scalaz.\/

trait CliParser {

  def parse(args: List[String]): ParseFailure \/ ChessProblem

}

case class ParseFailure(msg: String)
