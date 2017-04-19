package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess.{Chessboard, Board, Piece}

trait ChessChallengeSolver {

  def solve(board: Board, pieces: List[(Piece, Int)]): List[Chessboard]

}
