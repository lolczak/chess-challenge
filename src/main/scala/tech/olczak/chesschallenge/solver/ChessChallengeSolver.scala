package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess.Chessboard

trait ChessChallengeSolver {

  def solve(chessConfig: ChessConfig): List[Chessboard]

}
