package tech.olczak.chesschallenge.chess

import org.scalatest.{FlatSpec, Matchers}

import scalaz._
import Scalaz._

class ChessboardShowSpec extends FlatSpec with Matchers {

  "A chessboard" should "convert empty arrangement to textual representation" in {
    //given
    val chessboard = Chessboard.empty(Board(5, 5))
    //when
    val result = chessboard.shows
    //then
    result shouldBe
      """_ _ _ _ _
        |_ _ _ _ _
        |_ _ _ _ _
        |_ _ _ _ _
        |_ _ _ _ _
        |""".stripMargin
  }

  it should "convert a piece arrangement to textual representation" in {
    //given
    val chessboard = Chessboard(Board(4, 4), Set(Placement(King, Square(0, 0)), Placement(Knight, Square(1, 2)), Placement(Queen, Square(3, 3))))
    //when
    val result = chessboard.shows
    //then
    result shouldBe
      """K _ _ _
        |_ _ N _
        |_ _ _ _
        |_ _ _ Q
        |""".stripMargin
  }

}
