package tech.olczak.chesschallenge.solver

import org.scalatest.{Matchers, FlatSpec}
import org.scalacheck.Gen
import tech.olczak.chesschallenge.chess._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class BacktrackingSolverSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "A backtracking solver" should "not find a solution when there is no pieces" in {
    //when
    val solutions = BacktrackingSolver.solve(BoardDimension(3, 3), List.empty)
    //then
    solutions shouldBe empty
  }

  it should "find as many solutions as there is squares on the board when one piece is provided" in new TestContext {
    forAll(boardGen) { case d@BoardDimension(rankCount, fileCount) =>
      //when
      val solutions = BacktrackingSolver.solve(d, List(Queen -> 1))
      //then
      solutions should have size fileCount * rankCount
      solutions should contain theSameElementsAs (for (rank <- 0 until rankCount; file <- 0 until fileCount) yield Arrangement(d, List(PiecePosition(Queen, Square(rank, file)))))
    }
  }

  trait TestContext {

    val boardGen =
      for {
        rankCount <- Gen.choose(1, 10)
        fileCount <- Gen.choose(1, 10)
      } yield BoardDimension(rankCount, fileCount)


  }

}
