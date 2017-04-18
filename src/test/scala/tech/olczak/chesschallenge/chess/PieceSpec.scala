package tech.olczak.chesschallenge.chess

import org.scalacheck.Gen
import org.scalatest.Inspectors._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class PieceSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "A king" should "threaten one square in any direction" in new TestContext {
    forAll(squareGen) { occupied =>
      //given
      val threatenedSquares = for (x <- -1 to 1; y <- -1 to 1) yield Square(occupied.rank + x, occupied.file + y)
      val safeSquares = SampleBoard.allSquares.diff(threatenedSquares)
      //then
      forEvery(threatenedSquares) { square => assert(King.isThreatened(occupied)(square)) }
      forEvery(safeSquares) { square => assert(!King.isThreatened(occupied)(square)) }
    }
  }

  "A rook" should "threaten any number of squares along any rank or file" in new TestContext {
    forAll(squareGen) { occupied =>
      //given
      val threatenedSquares = SampleBoard.allSquares filter { case Square(rank, file) => occupied.rank == rank || occupied.file == file }
      val safeSquares = SampleBoard.allSquares.diff(threatenedSquares)
      //then
      forEvery(threatenedSquares) { square => assert(Rook.isThreatened(occupied)(square)) }
      forEvery(safeSquares) { square => assert(!Rook.isThreatened(occupied)(square)) }
    }
  }

  trait TestContext {

    val SampleBoard = Board(10, 10)

    val squareGen =
      for {
        rank <- Gen.choose(1, 10)
        file <- Gen.choose(1, 10)
      } yield Square(rank, file)

  }

}