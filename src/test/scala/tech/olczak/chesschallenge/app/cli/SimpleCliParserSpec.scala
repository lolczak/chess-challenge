package tech.olczak.chesschallenge.app.cli

import org.scalatest.{FlatSpec, Matchers}
import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scalaz.Scalaz._
import scalaz._

class SimpleCliParserSpec extends FlatSpec with Matchers {

  "A cli parser" should "report an error when there is insufficient number of args" in {
    SimpleCliParser.parse(List.empty) shouldBe TooLessArguments.failureNel
    SimpleCliParser.parse(List("3")) shouldBe TooLessArguments.failureNel
  }

  it should "fail when rank count is not number" in {
    SimpleCliParser.parse(List("a", "3", "K2")) shouldBe InvalidRankCount.failureNel
    SimpleCliParser.parse(List("##@", "3", "K2")) shouldBe InvalidRankCount.failureNel
    SimpleCliParser.parse(List(":", "3", "K2")) shouldBe InvalidRankCount.failureNel
    SimpleCliParser.parse(List("122a", "3", "K2")) shouldBe InvalidRankCount.failureNel
  }

  it should "fail when file count is not number" in {
    SimpleCliParser.parse(List("4", "a", "K2")) shouldBe InvalidFileCount.failureNel
    SimpleCliParser.parse(List("5", "##@", "K2")) shouldBe InvalidFileCount.failureNel
    SimpleCliParser.parse(List("3", ":", "K2")) shouldBe InvalidFileCount.failureNel
    SimpleCliParser.parse(List("3", "122a", "K2")) shouldBe InvalidFileCount.failureNel
  }

  it should "fail when piece group is incorrect" in {
    SimpleCliParser.parse(List("4", "4", "K")) shouldBe PieceGroupFormatFailure("K").failureNel
    SimpleCliParser.parse(List("5", "5", "Q")) shouldBe PieceGroupFormatFailure("Q").failureNel
    SimpleCliParser.parse(List("3", "7", "2K")) shouldBe PieceGroupFormatFailure("2K").failureNel
    SimpleCliParser.parse(List("3", "7", "A4")) shouldBe PieceGroupFormatFailure("A4").failureNel
    SimpleCliParser.parse(List("3", "8", "3243")) shouldBe PieceGroupFormatFailure("3243").failureNel
  }

  it should "parse correct args" in {
    SimpleCliParser.parse(List("8", "8", "Q8")) shouldBe ChessConfig(Board(8, 8), List((Queen, 8))).successNel
    SimpleCliParser.parse(List("3", "3", "K2", "R1")) shouldBe ChessConfig(Board(3, 3), List((King, 2), (Rook, 1))).successNel
    SimpleCliParser.parse(List("4", "4", "R2", "N4")) shouldBe ChessConfig(Board(4, 4), List((Rook, 2), (Knight, 4))).successNel
    SimpleCliParser.parse(List("7", "7", "K2", "Q2", "B2", "N1")) shouldBe ChessConfig(Board(7, 7), List((King, 2), (Queen, 2), (Bishop, 2), (Knight, 1))).successNel
  }

  it should "fail when args contain repeated pieces" in {
    SimpleCliParser.parse(List("3", "8", "K2", "K8")) shouldBe PieceDuplication.failureNel
    SimpleCliParser.parse(List("7", "7", "K2", "Q2", "B2", "Q1")) shouldBe PieceDuplication.failureNel
  }

}
