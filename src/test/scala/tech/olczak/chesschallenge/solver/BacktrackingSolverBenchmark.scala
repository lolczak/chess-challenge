package tech.olczak.chesschallenge.solver

import org.scalameter.api._
import tech.olczak.chesschallenge.chess._

object BacktrackingSolverBenchmark extends PerformanceTest.Quickbenchmark {

  val chess = Gen.single("chess-problem")((Board(7,7), List(King -> 2, Queen -> 2, Bishop -> 2, Knight -> 1)))

  performance of "BacktrackingSolver" in {
    measure method "solve" config (
        exec.benchRuns -> 4,
        exec.maxWarmupRuns -> 4,
        exec.minWarmupRuns -> 1
      ) in {
      using(chess) in { case (board, pieces) =>
        val results = BacktrackingSolver.solve(ChessConfig(board, pieces))
        println(s"Found: ${results.size}")
      }
    }
  }

}
