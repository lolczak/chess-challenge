package tech.olczak.chesschallenge.app.cli

import org.scalatest.{FlatSpec, Matchers}
import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz.{-\/, \/-}

class SimpleCliParserSpec extends FlatSpec with Matchers {

  "A cli parser" should "report an error when there is insufficient number of args" in {
    SimpleCliParser.parse(List.empty) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when rank count is not number" in {
    SimpleCliParser.parse(List("a", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("##@", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List(":", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("122a", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when file count is not number" in {
    SimpleCliParser.parse(List("4", "a", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("5", "##@", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", ":", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "122a", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when piece group is incorrect" in {
    SimpleCliParser.parse(List("4", "4", "K")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("5", "5", "Q")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "7", "2K")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "7", "A4")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "8", "3243")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "parse correct args" in {
    SimpleCliParser.parse(List("8", "8", "Q8")) should matchPattern { case \/-(ChessConfig(Board(8, 8), List((Queen, 8)))) => }
    SimpleCliParser.parse(List("3", "3", "K2", "R1")) should matchPattern { case \/-(ChessConfig(Board(3, 3), List((King, 2), (Rook, 1)))) => }
    SimpleCliParser.parse(List("4", "4", "R2", "N4")) should matchPattern { case \/-(ChessConfig(Board(4, 4), List((Rook, 2), (Knight, 4)))) => }
    SimpleCliParser.parse(List("7", "7", "K2", "Q2", "B2", "N1")) should matchPattern { case \/-(ChessConfig(Board(7, 7), List((King, 2), (Queen, 2), (Bishop, 2), (Knight, 1)))) => }
  }

  it should "fail when args contain repeated pieces" in {
    SimpleCliParser.parse(List("3", "8", "K2", "K8")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("7", "7", "K2", "Q2", "B2", "Q1")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

}
