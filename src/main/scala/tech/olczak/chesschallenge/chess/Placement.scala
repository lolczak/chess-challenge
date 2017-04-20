package tech.olczak.chesschallenge.chess

/**
  * Represents a piece placement on the chessboard.
  *
  * @param piece
  * @param square
  */
case class Placement(piece: Piece, square: Square) {

  def isSafe(tested: Square): Boolean = !piece.isThreatened(square)(tested)

}
