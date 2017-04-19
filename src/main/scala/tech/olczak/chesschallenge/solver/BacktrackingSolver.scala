package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: Board, pieceCounts: List[(Piece, Int)]): List[Chessboard] = {
    if (pieceCounts.isEmpty) List.empty
    else {
      val pieces = pieceCounts flatMap { case (piece, count) => for (_ <- 1 to count) yield piece }

      board.allSquares.combinations(pieces.size).toList.par
        .flatMap(_.permutations.toList)
        .map(_.zip(pieces))
        .map(_.map { case (s, p) => PiecePosition(p, s)})
        .map(p => Chessboard(board, p.toSet))
        .filter(_.isSafe)
        .toSet
        .toList
    }
  }

}
