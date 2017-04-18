package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: Board, pieces: List[(Piece, Int)]): List[Arrangement] = {
    if (pieces.isEmpty) List.empty
    else {
      val (piece, count) = pieces.head

      board.allSquares.map(square => Arrangement(board, List(PiecePosition(piece, square))))
    }
  }

}
