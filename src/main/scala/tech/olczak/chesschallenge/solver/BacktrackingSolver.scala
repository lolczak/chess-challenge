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
        .filter(positions => positions.combinations(2).forall { case List(p1, p2) => !p1.piece.isThreatened(p1.square)(p2.square) &&  !p2.piece.isThreatened(p2.square)(p1.square)} )
        .map(p => Chessboard(board, p.toSet))
        .toSet
        .toList
    }
  }

}
