package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.postfixOps
import scalaz.Scalaz._
import scalaz.Validation.fromTryCatchNonFatal
import scalaz._

object SimpleCliParser extends CliParser {

  val PieceRegEx = """([KQRNB])(\d+)""".r

  override def parse(args: List[String]): ParseFailure \/ ChessConfig =
    args match {
      case rankStr :: fileStr :: pieces => parseConfig(rankStr, fileStr, pieces)
      case _                            => ParseFailure("Too less arguments").left
    }


  private def parseConfig(rankStr: String, fileStr: String, pieces: List[String]): ParseFailure \/ ChessConfig = {
    val board = parseBoard(rankStr, fileStr)
    val groups = pieces.traverseU(parsePiece)
    val config = (board ⊛ groups)(ChessConfig.apply)
    config leftMap (errors => ParseFailure(errors.toList.mkString(", "))) disjunction
  }

  private def parseBoard(rankStr: String, fileStr: String): ValidationNel[String, Board] = {
    val rank = parseInt(rankStr) leftMap (_ => "Rank count is not a number") toValidationNel
    val file = parseInt(fileStr) leftMap (_ => "File count is not a number") toValidationNel

    (rank ⊛ file)(Board.apply)
  }

  private def parseInt(str: String): ValidationNel[String, Int] =
    fromTryCatchNonFatal(str.toInt).leftMap(_ => s"$str is not a number").toValidationNel

  private def parsePiece(pieceStr: String): ValidationNel[String, (Piece, Int)] =
    pieceStr match {
      case PieceRegEx(symbol, count) => (parseSymbol(symbol) ⊛ parseInt(count)).tupled
      case _ => s"Wrong format of piece group: $pieceStr".failureNel
    }

  private val parseSymbol: String => ValidationNel[String, Piece] = {
    case "K"    => King.successNel
    case "Q"    => Queen.successNel
    case "B"    => Bishop.successNel
    case "R"    => Rook.successNel
    case "N"    => Knight.successNel
    case symbol => s"Unknown piece symbol $symbol".failureNel
  }

}
