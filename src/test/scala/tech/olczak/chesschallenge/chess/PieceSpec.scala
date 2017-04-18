package tech.olczak.chesschallenge.chess

import org.scalatest.{FlatSpec, Matchers}

class PieceSpec extends FlatSpec with Matchers {

  "A king" should "threaten one square in any direction" in {
    //given
    val current = Square(3, 4)
    val threatened = for (x <- -1 to 1; y <- -1 to 1) yield Square(current.rank + x, current.file + y)
    val other = Board(5, 5).allSquares.diff(threatened)
    //then
    threatened.forall(King.isThreatened(current)) shouldBe true
    other.forall(!King.isThreatened(current)(_)) shouldBe true
  }

}
