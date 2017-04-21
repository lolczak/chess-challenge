package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.chess.Board
import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz.Validation.fromTryCatchNonFatal
import scalaz._
import Scalaz._

object SimpleCliParser extends CliParser {

  override def parse(args: List[String]): \/[ParseFailure, ChessConfig] = {
    if (args.size < 3) -\/(ParseFailure("Too less arguments"))
    else {
      val (rankStr :: fileStr :: pieces) = args
      val rank = fromTryCatchNonFatal(rankStr.toInt).leftMap(_ => "Rank count is not a number")
      (rank map { (rank: Int) => ChessConfig(Board(rank, 3), List.empty) }).leftMap(ParseFailure).disjunction
    }
  }


}
