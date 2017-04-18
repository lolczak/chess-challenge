package tech.olczak.chesschallenge.solver

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import tech.olczak.chesschallenge.chess._

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
      solutions should contain theSameElementsAs genAllSquares(board).map(s => Arrangement(board, List(PiecePosition(Queen, s))))
    }
  }

  trait TestContext {

    val objectUnderTest = BacktrackingSolver

    val boardGen =
      for {
        rankCount <- Gen.choose(1, 10)
        fileCount <- Gen.choose(1, 10)
      } yield Board(rankCount, fileCount)

    def genAllSquares(dimension: Board) =
      for (rank <- 0 until dimension.rankCount; file <- 0 until dimension.fileCount) yield Square(rank, file)

  }

}
