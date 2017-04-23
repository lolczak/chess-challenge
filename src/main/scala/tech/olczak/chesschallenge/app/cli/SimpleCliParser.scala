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

  override def parse(args: List[String]): ValidationNel[ParseFailure, ChessConfig] =
    args match {
      case rank :: file :: pieces => parseConfig(rank, file, pieces)
      case _                      => (TooLessArguments : ParseFailure).failureNel
    }

  private def parseConfig(rank: String, file: String, pieces: List[String]): ValidationNel[ParseFailure, ChessConfig] = {
    val board = parseBoard(rank, file)
    val groups = parsePieces(pieces)
    (board ⊛ groups)(ChessConfig.apply)
  }

  private def parsePieces(symbols: List[String]): ValidationNel[ParseFailure, List[(Piece, Int)]] = {
    val pieceGroups = symbols traverseU parsePiece
    val duplicationFailure = NonEmptyList(PieceDuplication : ParseFailure)
    val duplicationTest = (pieces: List[(Piece, Int)]) => pieces.groupBy(_._1).forall { case (_, group) => group.size == 1 }
    pieceGroups.ensure(duplicationFailure)(duplicationTest)
  }

  private def parseBoard(rankTerm: String, fileTerm: String): ValidationNel[ParseFailure, Board] = {
    val rank = parseInt(rankTerm).leftMap(_ => InvalidRankCount : ParseFailure).toValidationNel
    val file = parseInt(fileTerm).leftMap(_ => InvalidFileCount : ParseFailure).toValidationNel
    (rank ⊛ file)(Board.apply)
  }

  private def parseInt(str: String): ValidationNel[ParseFailure, Int] =
    fromTryCatchNonFatal(str.toInt).leftMap(_ => NumberExpected(str) : ParseFailure).toValidationNel

  private def parsePiece(pieceStr: String): ValidationNel[ParseFailure, (Piece, Int)] =
    pieceStr match {
      case PieceRegEx(symbol, count) => (parseSymbol(symbol) ⊛ parseInt(count)).tupled
      case _                         => (PieceGroupFormatFailure(pieceStr) : ParseFailure).failureNel
    }

  private val parseSymbol: String => ValidationNel[ParseFailure, Piece] = {
    case KingSymbol   => King.successNel
    case QueenSymbol  => Queen.successNel
    case BishopSymbol => Bishop.successNel
    case RookSymbol   => Rook.successNel
    case KnightSymbol => Knight.successNel
    case symbol       => (UnknownPiece(symbol) : ParseFailure).failureNel
  }

}
