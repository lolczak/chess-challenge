package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

import scala.annotation.tailrec

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: Board, pieceGroups: List[(Piece, Int)]): List[Chessboard] = {
    if (pieceGroups.isEmpty) List.empty
    else {
      val head :: tail = pieceGroups flatMap { case (piece, count) => for (_ <- 1 to count) yield piece }
      loopPieces(tail, expand(head, Chessboard.empty(board))).distinct
    }
  }

  @tailrec
  private def loopPieces(pieces: List[Piece], candidates: List[Chessboard]): List[Chessboard] = {
    if (pieces.isEmpty) candidates
    else {
      val piece = pieces.head
      loopPieces(pieces.tail, candidates flatMap (c => if(c.safeSquares.size >= pieces.size) expand(piece, c) else List.empty))
    }
  }

  private def expand(piece: Piece, chessboard: Chessboard): List[Chessboard] =
    for {
      square    <- chessboard.findSafeSquares(piece)
    } yield chessboard.placePiece(piece, square)

}
