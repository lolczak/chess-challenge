package tech.olczak.chesschallenge.solver

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import tech.olczak.chesschallenge.ChessObjectMother._
import tech.olczak.chesschallenge.chess._

class BacktrackingSolverSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "A backtracking solver" should "not find a solution when there is no pieces" in new TestContext {
    //when
    val solutions = objectUnderTest.solve(ChessConfigWithoutPieces)
    //then
    solutions shouldBe empty
  }

  it should "find as many solutions as there is squares on the board when one piece is provided" in new TestContext {
    forAll(boardGen) { case board@Board(rankCount, fileCount) =>
      //when
      val solutions = objectUnderTest.solve(ChessConfig(board, List(Queen -> 1)))
      //then
      solutions should contain theSameElementsAs genAllSquares(board).map(s => Chessboard(board, Set(Placement(Queen, s))))
    }
  }

  it should "solve real world problem" in new TestContext {
    forAll(RealWorldProblems) { case (config, expectedSolutions) =>
      //when
      val solutions = objectUnderTest.solve(config)
      //then
      solutions should contain theSameElementsAs expectedSolutions
    }
  }

  trait TestContext {

    val objectUnderTest = BacktrackingSolver

    val boardGen =
      for {
        rankCount <- Gen.choose(1, 8)
        fileCount <- Gen.choose(1, 8)
      } yield Board(rankCount, fileCount)

    def genAllSquares(dimension: Board) =
      for (rank <- 0 until dimension.rankCount; file <- 0 until dimension.fileCount) yield Square(rank, file)

    val RealWorldProblems = Gen oneOf Seq(TinyChessConfig -> TinyConfigSolutions, MediumChessConfig -> MediumConfigSolutions)

  }

}
