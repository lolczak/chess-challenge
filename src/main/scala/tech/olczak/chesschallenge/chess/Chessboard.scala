package tech.olczak.chesschallenge.chess

/**
  * Represents an arrangement of pieces on a chessboard.
  */
case class Chessboard(board: Board, placements: List[Placement], safeSquares: List[Square]) {

  lazy val isSafe =
    placements.toList.combinations(2).forall {
      case List(p1, p2) => !p1.piece.isThreatened(p1.square)(p2.square) && !p2.piece.isThreatened(p2.square)(p1.square)
    }

  def placePiece(piece: Piece, square: Square): Chessboard = placePiece(Placement(piece, square))

  def placePiece(placement: Placement): Chessboard =
    copy(placements = placement :: placements,
      safeSquares = safeSquares.filter(tested => placement.isSafe(tested) && tested != placement.square))

  def findSafeSquares(piece: Piece): List[Square] =
    safeSquares.filter(tested => placements.forall(placement => !piece.isThreatened(tested)(placement.square)))

  def findSafeGreaterSquares(piece: Piece, square: Square): List[Square] =
    safeSquares.filter(tested => tested > square && placements.forall(placement => !piece.isThreatened(tested)(placement.square)))

  lazy val toArrangement = Arrangement(board, placements.toSet)

}

object Chessboard {

  def apply(board: Board, placements: List[Placement]): Chessboard =
    placements.foldLeft(Chessboard.empty(board)) { case (chessboard, placement) => chessboard.placePiece(placement) }

  def empty(board: Board) = Chessboard(board, List.empty, board.allSquares)

}
