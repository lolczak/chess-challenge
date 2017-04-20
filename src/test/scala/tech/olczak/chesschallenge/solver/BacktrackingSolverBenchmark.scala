package tech.olczak.chesschallenge.solver

import org.scalameter.api._
import tech.olczak.chesschallenge.chess._

object BacktrackingSolverBenchmark extends PerformanceTest.Quickbenchmark {

  val chess = Gen.single("chess-problem")((Board(8, 8), List(Queen -> 8)))

  performance of "BacktrackingSolver" in {
    measure method "solve" in {
      using(chess) in { case (board, pieces) =>
        val results = BacktrackingSolver.solve(board, pieces)
        println(s"Found: ${results.size}")
      }
    }
  }

}
