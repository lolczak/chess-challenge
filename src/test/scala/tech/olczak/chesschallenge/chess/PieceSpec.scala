package tech.olczak.chesschallenge.chess

import org.scalacheck.Gen
import org.scalatest.Inspectors._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

class PieceSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "The king" should "move one square in any direction" in new TestContext {
    val KingMoves = (for (x <- -1 to 1; y <- -1 to 1) yield (x, y)).toList
    testPieceMoves(King, KingMoves)
  }

  "The rook" should "move any number of squares along any rank or file" in new TestContext {
    testPieceMoves(Rook, RankForwardMoves, RankBackwardMoves, FileForwardMoves, FileBackwardMoves)
  }

  "The knight" should "move two squares vertically and one square horizontally, or two squares horizontally and one square vertically" in new TestContext {
    val KnightMoves = List((2, 1), (2, -1), (-2, 1), (-2, -1), (1, 2), (1, -2), (-1, -2), (-1, 2))
    testPieceMoves(Knight, KnightMoves)
  }

  "The bishop" should "move any number of squares diagonally" in new TestContext {
    testPieceMoves(Bishop, DiagonalRfFfMoves, DiagonalRfFbMoves, DiagonalRbFfMoves, DiagonalRbFbMoves)
  }

  "The queen" should "move any number of squares along rank, file, or diagonal" in new TestContext {
    testPieceMoves(Queen, DiagonalRfFfMoves, DiagonalRfFbMoves, DiagonalRbFfMoves, DiagonalRbFbMoves, RankForwardMoves,
      RankBackwardMoves, FileForwardMoves, FileBackwardMoves)
  }

  trait TestContext {

    val TestBoard = Board(10, 10)

    val squareGen =
      for {
        rank <- Gen.choose(0, 9)
        file <- Gen.choose(0, 9)
      } yield Square(rank, file)

    def testPieceMoves(piece: Piece, moves: List[(Int, Int)]): Unit = {
      val stream = moves.map(Stream(_))
      testPieceMoves(piece, stream: _*)
    }

    def testPieceMoves(piece: Piece, moveStreams: Stream[(Int, Int)]*): Unit = {
      forAll(squareGen) { occupied =>
        //given
        val threatenedSquares = moveStreams.flatMap(stream => stream map { case (x, y) => Square(occupied.rank + x, occupied.file + y) } takeWhile isOnBoard toSeq)
        val safeSquares = TestBoard.allSquares.diff(threatenedSquares)
        //then
        forEvery(threatenedSquares) { square => assert(piece.isThreatened(occupied)(square)) }
        forEvery(safeSquares) { square => assert(!piece.isThreatened(occupied)(square)) }
      }
    }

    def isOnBoard(square: Square) =
      square.rank >= 0 && square.rank < TestBoard.rankCount && square.file >= 0 && square.file < TestBoard.fileCount

    val DiagonalRfFfMoves = genMoves(1, 1)
    val DiagonalRfFbMoves = genMoves(1, -1)
    val DiagonalRbFfMoves = genMoves(-1, 1)
    val DiagonalRbFbMoves = genMoves(-1, -1)

    val RankForwardMoves = genMoves(1, 0)
    val RankBackwardMoves = genMoves(-1, 0)
    val FileForwardMoves = genMoves(0, +1)
    val FileBackwardMoves = genMoves(0, -1)

    def genMoves(rowAdd: Int, colAdd: Int) =
      Stream.iterate((0, 0)) { case (row, col) => (row + rowAdd, col + colAdd) }

  }

}
