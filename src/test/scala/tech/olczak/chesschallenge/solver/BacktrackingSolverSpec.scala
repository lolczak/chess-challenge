package tech.olczak.chesschallenge.solver

import org.scalatest.{Matchers, FlatSpec}
import tech.olczak.chesschallenge.chess.BoardDimension

class BacktrackingSolverSpec extends FlatSpec with Matchers {

  "A backtracking solver" should "not find a solution when there is no pieces" in {
    //when
    val solutions = BacktrackingSolver.solve(BoardDimension(3, 3), List.empty)
    //then
    solutions shouldBe empty
  }

}
