package tech.olczak.chesschallenge.solver

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import tech.olczak.chesschallenge.chess._

import scala.language.implicitConversions

class BacktrackingSolverSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "A backtracking solver" should "not find a solution when there is no pieces" in new TestContext {
    //when
    val solutions = objectUnderTest.solve(Board(3, 3), List.empty)
    //then
    solutions shouldBe empty
  }

  it should "find as many solutions as there is squares on the board when one piece is provided" in new TestContext {
    forAll(boardGen) { case board@Board(rankCount, fileCount) =>
      //when
      val solutions = objectUnderTest.solve(board, List(Queen -> 1))
      //then
      solutions should contain theSameElementsAs genAllSquares(board).map(s => Arrangement(board, Set(PiecePosition(Queen, s))))
    }
  }

  it should "solve real world problem" in new TestContext {
    forAll(RealWorldProblems) { case (board, pieces, expectedSolutions) =>
      //when
      val solutions = objectUnderTest.solve(board, pieces)
      //then
      solutions should contain only (expectedSolutions: _*)
    }
  }

  trait TestContext {

    val objectUnderTest = BacktrackingSolver

    val boardGen =
      for {
        rankCount <- Gen.choose(1, 2)
        fileCount <- Gen.choose(1, 2)
      } yield Board(rankCount, fileCount)

    def genAllSquares(dimension: Board) =
      for (rank <- 0 until dimension.rankCount; file <- 0 until dimension.fileCount) yield Square(rank, file)

    implicit def toPiecePosition(tuple: (Piece, (Int, Int))): PiecePosition = PiecePosition(tuple._1, Square(tuple._2._1, tuple._2._2))

    val RealWorldProblems = Gen oneOf Seq(
      (Board(3, 3), List(King -> 2, Rook -> 1), List(
        Arrangement(Board(3, 3), Set(King ->(0, 0), King ->(0, 2), Rook ->(2, 1))),
        Arrangement(Board(3, 3), Set(King ->(0, 0), Rook ->(1, 2), King ->(2, 0))),
        Arrangement(Board(3, 3), Set(King ->(0, 2), Rook ->(1, 0), King ->(2, 2))),
        Arrangement(Board(3, 3), Set(Rook ->(0, 1), King ->(2, 0), King ->(2, 2)))
      ))
    )

  }

}
