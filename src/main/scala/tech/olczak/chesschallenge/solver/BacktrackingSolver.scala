package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

import scala.annotation.tailrec

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: Board, pieceCounts: List[(Piece, Int)]): List[Chessboard] = {
    if (pieceCounts.isEmpty) List.empty
    else {
      val head :: tail = pieceCounts flatMap { case (piece, count) => for (_ <- 1 to count) yield piece }
      loopPieces(tail, expand(head, Chessboard.empty(board))).distinct
    }
  }

  @tailrec
  private def loopPieces(pieces: List[Piece], candidates: List[Chessboard]): List[Chessboard] = {
    if (pieces.isEmpty) candidates
    else {
      val piece = pieces.head
      loopPieces(pieces.tail, candidates flatMap (c => expand(piece, c)))
    }
  }

  private def expand(piece: Piece, chessboard: Chessboard): List[Chessboard] = {
    for {
      square    <- chessboard.board.allSquares.diff(chessboard.piecePositions.map(_.square).toList)
      candidate =  chessboard.copy(piecePositions = chessboard.piecePositions + PiecePosition(piece, square))
      if candidate.isSafe
    } yield candidate
  }

}
