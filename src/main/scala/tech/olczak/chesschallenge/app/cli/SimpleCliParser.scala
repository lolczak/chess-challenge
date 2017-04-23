package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.app.constant.Constants._
import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.postfixOps
import scalaz.Scalaz._
import scalaz.Validation.fromTryCatchNonFatal
import scalaz._

object SimpleCliParser extends CliParser {

  val PieceRegEx = s"([$KingSymbol$QueenSymbol$RookSymbol$KnightSymbol$BishopSymbol])(\\d+)".r

  override def parse(args: List[String]): ParseFailure \/ ChessConfig =
    args match {
      case rank :: file :: pieces => parseConfig(rank, file, pieces)
      case _                      => ParseFailure("Too less arguments").left
    }

  private def parseConfig(rankStr: String, fileStr: String, pieces: List[String]): ParseFailure \/ ChessConfig = {
    val board = parseBoard(rankStr, fileStr)
    val groups = parsePieces(pieces)
    val config = (board ⊛ groups)(ChessConfig.apply)
    config leftMap (errors => ParseFailure(errors.toList.mkString(", "))) disjunction
  }

  private def parsePieces(symbols: List[String]): ValidationNel[String, List[(Piece, Int)]] = {
    val pieceGroups = symbols.traverseU(parsePiece)
    val duplicationFailure = NonEmptyList("There are duplications of pieces")
    val duplicationTest = (pieces: List[(Piece, Int)]) => pieces.groupBy(_._1).forall { case (_, group) => group.size == 1 }
    pieceGroups.ensure(duplicationFailure)(duplicationTest)
  }

  private def parseBoard(rankStr: String, fileStr: String): ValidationNel[String, Board] = {
    val rank = parseInt(rankStr).leftMap(_ => "Rank count is not a number").toValidationNel
    val file = parseInt(fileStr).leftMap(_ => "File count is not a number").toValidationNel
    (rank ⊛ file)(Board.apply)
  }

  private def parseInt(str: String): ValidationNel[String, Int] =
    fromTryCatchNonFatal(str.toInt).leftMap(_ => s"$str is not a number").toValidationNel

  private def parsePiece(pieceStr: String): ValidationNel[String, (Piece, Int)] =
    pieceStr match {
      case PieceRegEx(symbol, count) => (parseSymbol(symbol) ⊛ parseInt(count)).tupled
      case _                         => s"Wrong format of piece group: $pieceStr".failureNel
    }

  private val parseSymbol: String => ValidationNel[String, Piece] = {
    case KingSymbol   => King.successNel
    case QueenSymbol  => Queen.successNel
    case BishopSymbol => Bishop.successNel
    case RookSymbol   => Rook.successNel
    case KnightSymbol => Knight.successNel
    case symbol       => s"Unknown piece symbol: $symbol".failureNel
  }

}
