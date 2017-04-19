package tech.olczak.chesschallenge.chess

/**
  * Represents an arrangement of pieces on a chessboard.
  */
case class Chessboard(board: Board, piecePositions: Set[PiecePosition]) {

  lazy val isSafe =
    piecePositions.toList.combinations(2).forall {
      case List(p1, p2) => !p1.piece.isThreatened(p1.square)(p2.square) && !p2.piece.isThreatened(p2.square)(p1.square)
    }

}

object Chessboard {

  def empty(board: Board) = Chessboard(board, Set.empty)

}
