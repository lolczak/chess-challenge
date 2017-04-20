package tech.olczak.chesschallenge.chess

import org.scalatest.{Matchers, FlatSpec}
import scalaz._
import Scalaz._

class ChessboardShowSpec extends FlatSpec with Matchers {

  "A chessboard" should "convert empty arrangement to textual representation" in {
    //given
    val chessboard = Chessboard.empty(Board(5,5))
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

}
