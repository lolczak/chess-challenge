package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess._

import scala.annotation.tailrec

object BacktrackingSolver extends ChessChallengeSolver {

  override def solve(board: Board, pieceGroups: List[(Piece, Int)]): List[Chessboard] = {
    if (pieceGroups.isEmpty) List.empty
    else {
      val sorted = pieceGroups.sortBy(_._1.dangerRank)
      val (piece, count) = sorted.head
      loopPieceGroups(sorted.tail, expand(piece, count, Chessboard.empty(board)))
    }
  }

  @tailrec
  private def loopPieceGroups(pieceGroups: List[(Piece, Int)], candidates: List[Chessboard]): List[Chessboard] = {
    if (pieceGroups.isEmpty) candidates
    else {
      val (piece, count) = pieceGroups.head
      val pieceCount = pieceGroups.map(_._2).sum
      val expandedCandidates =
        candidates.par flatMap (c => if (c.safeSquares.size >= pieceCount) expand(piece, count, c) else List.empty)
      loopPieceGroups(pieceGroups.tail, expandedCandidates.toList)
    }
  }

  private def expand(piece: Piece, pieceCount: Int, chessboard: Chessboard): List[Chessboard] = {
    @tailrec
    def loopSamePieces(pieceCount: Int, candidates: List[(Chessboard, Square)]): List[Chessboard] = {
      if (pieceCount == 0) candidates.map(_._1)
      else {
        val expandedCandidates = candidates.foldLeft(List.empty[(Chessboard, Square)]) {
          case (acc, (candidate, previousSquare)) =>
            loopSquares(piece, candidate, candidate.findSafeGreaterSquares(piece, previousSquare), acc)
        }
        loopSamePieces(pieceCount - 1, expandedCandidates)
      }
    }

    val candidates = loopSquares(piece, chessboard, chessboard.findSafeSquares(piece), List.empty)

    loopSamePieces(pieceCount - 1, candidates)
  }

  @tailrec
  private def loopSquares(piece: Piece, chessboard: Chessboard, squares: List[Square], candidates: List[(Chessboard, Square)]): List[(Chessboard, Square)] =
    if (squares.isEmpty) candidates
    else loopSquares(piece, chessboard, squares.tail, (chessboard.placePiece(piece, squares.head), squares.head) :: candidates)

}
