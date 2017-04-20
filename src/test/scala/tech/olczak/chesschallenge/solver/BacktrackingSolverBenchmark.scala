package tech.olczak.chesschallenge.solver

import org.scalameter.api._
import tech.olczak.chesschallenge.chess._

object BacktrackingSolverBenchmark extends PerformanceTest.Quickbenchmark {

  val chess = Gen.single("chess-problem")((Board(10, 10), List(Queen -> 10)))

  performance of "BacktrackingSolver" in {
    measure method "solve" config (
        exec.benchRuns -> 4,
        exec.maxWarmupRuns -> 4,
        exec.minWarmupRuns -> 1
      ) in {
      using(chess) in { case (board, pieces) =>
        val results = BacktrackingSolver.solve(board, pieces)
        println(s"Found: ${results.size}")
      }
    }
  }

}
