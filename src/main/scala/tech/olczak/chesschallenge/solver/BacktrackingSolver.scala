package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: BoardDimension, pieces: List[(Piece, Int)]): List[Arrangement] = {
    if (pieces.isEmpty) List.empty
    else {
      val (piece, count) = pieces.head
      val allSquares =
        for {
          rank <- 0 until board.rankCount
          file <- 0 until board.fileCount
        } yield Square(rank, file)

      allSquares.map(square => Arrangement(board, List(PiecePosition(piece, square)))).toList
    }
  }

}
