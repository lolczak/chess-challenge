package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess.{Arrangement, Piece, BoardDimension}

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: BoardDimension, pieces: List[(Piece, Int)]): List[Arrangement] = List.empty

}
