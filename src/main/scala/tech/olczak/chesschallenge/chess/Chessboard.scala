package tech.olczak.chesschallenge.chess

/**
  * Represents an arrangement of pieces on a chessboard.
  */
case class Chessboard(board: Board, placements: List[Placement], safeSquares: List[Square]) {

  def placePiece(piece: Piece, square: Square): Chessboard = placePiece(Placement(piece, square))

  def placePiece(placement: Placement): Chessboard =
    copy(placements = placement :: placements,
      safeSquares = safeSquares.filter(tested => placement.isSafe(tested) && tested != placement.square))

  def findSafeSquares(piece: Piece): List[Square] =
    safeSquares.filter(tested => placements.forall(placement => !piece.isThreatened(tested)(placement.square)))

  def findSafeGreaterSquares(piece: Piece, square: Square): List[Square] =
    safeSquares.filter(tested => tested > square && placements.forall(placement => !piece.isThreatened(tested)(placement.square)))

  override def equals(obj: scala.Any): Boolean = obj match {
    case Chessboard(otherBoard, otherPlacement, _) => board == otherBoard && placements.toSet == otherPlacement.toSet
    case _                                         => false
  }

}

object Chessboard {

  def apply(board: Board, placements: Set[Placement]): Chessboard =
    placements.foldLeft(Chessboard.empty(board)) { case (chessboard, placement) => chessboard.placePiece(placement) }

  def empty(board: Board) = Chessboard(board, List.empty, board.allSquares)

}
