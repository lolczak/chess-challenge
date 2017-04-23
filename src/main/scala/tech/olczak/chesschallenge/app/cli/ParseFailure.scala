package tech.olczak.chesschallenge.app.cli

import tech.olczak.chesschallenge.app.constant.Messages._
import scalaz.Show

sealed trait ParseFailure

case class UnknownPiece(symbol: String)          extends ParseFailure
case class PieceGroupFormatFailure(term: String) extends ParseFailure
case class NumberExpected(term: String)          extends ParseFailure
case object InvalidRankCount                     extends ParseFailure
case object InvalidFileCount                     extends ParseFailure
case object TooLessArguments                     extends ParseFailure
case object PieceDuplication                     extends ParseFailure

object ParseFailure {

  implicit val parseFailureShow = new Show[ParseFailure] {

    override def shows(failure: ParseFailure): String =
      failure match {
        case UnknownPiece(symbol)          => UnknownPieceMsg format symbol
        case PieceGroupFormatFailure(term) => PieceGroupFormatFailureMsg format term
        case NumberExpected(term)          => NumberExpectedMsg format term
        case InvalidRankCount              => InvalidRankCountMsg
        case InvalidFileCount              => InvalidFileCountMsg
        case TooLessArguments              => TooLessArgumentsMsg
        case PieceDuplication              => PieceDuplicationMsg
      }

  }

}