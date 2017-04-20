package tech.olczak.chesschallenge.chess

/**
  * Represents an arrangement of pieces on a chessboard.
  */
case class Chessboard(board: Board, piecePositions: Set[PiecePosition], safeSquares: List[Square]) {

  lazy val isSafe =
    piecePositions.toList.combinations(2).forall {
      case List(p1, p2) => !p1.piece.isThreatened(p1.square)(p2.square) && !p2.piece.isThreatened(p2.square)(p1.square)
    }

  lazy val unoccupiedSquares = board.allSquares.diff(piecePositions.map(_.square).toList)

  def placePiece(piece: Piece, square: Square): Chessboard =
    copy(piecePositions = piecePositions + PiecePosition(piece, square),
      safeSquares = safeSquares.filter(tested => !piece.isThreatened(square)(tested) && tested != square))

}

object Chessboard {

  def apply(board: Board, piecePositions: Set[PiecePosition]): Chessboard = {
    val safeSqaures = board.allSquares.filter(tested => piecePositions.forall(position => !position.piece.isThreatened(position.square)(tested) && tested != position.square ))
    Chessboard(board, piecePositions, safeSqaures)
  }

  def empty(board: Board) = Chessboard(board, Set.empty, board.allSquares)

}
