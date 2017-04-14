package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess.{Arrangement, Piece, BoardDimension}

trait ChessChallengeSolver {

  def solve(board: BoardDimension, pieces: List[(Piece, Int)]): List[Arrangement]

}
