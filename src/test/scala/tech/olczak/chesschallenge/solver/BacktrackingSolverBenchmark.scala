package tech.olczak.chesschallenge.solver

import org.scalameter.api._
import tech.olczak.chesschallenge.chess._

object BacktrackingSolverBenchmark extends PerformanceTest.Quickbenchmark {

  val chess = Gen.single("chess-problem")((Board(4, 4), List(Rook -> 2, Knight -> 4)))

  performance of "BacktrackingSolver" in {
    measure method "solve" in {
      using(chess) in { case (board, pieces) =>
        BacktrackingSolver.solve(board, pieces)
      }
    }
  }

}
