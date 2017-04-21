package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.chess.{Board, King, Piece}
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.postfixOps
import scalaz.Scalaz._
import scalaz.Validation.fromTryCatchNonFatal
import scalaz._

object SimpleCliParser extends CliParser {

  val PieceRegEx = """([KQRNB])(\d+)""".r

  override def parse(args: List[String]): \/[ParseFailure, ChessConfig] = {
    if (args.size < 3) -\/(ParseFailure("Too less arguments"))
    else {
      val (rankStr :: fileStr :: pieces) = args
      val rank = fromTryCatchNonFatal(rankStr.toInt).leftMap(_ => "Rank count is not a number").toValidationNel
      val file = fromTryCatchNonFatal(fileStr.toInt).leftMap(_ => "File count is not a number").toValidationNel
      val groups = pieces.traverseU(parsePiece)
      val board = (rank ⊛ file) { Board(_, _) }
      val config = (board ⊛ groups) { ChessConfig(_, _) }
      config leftMap (errors => ParseFailure(errors.toList.mkString(", "))) disjunction
    }
  }

  private def parsePiece(pieceStr: String): ValidationNel[String, (Piece, Int)] =
    pieceStr match {
      case PieceRegEx(symbol, count) => (King -> 1).successNel
      case _ => s"Wrong format of piece group: $pieceStr".failureNel
    }


}
