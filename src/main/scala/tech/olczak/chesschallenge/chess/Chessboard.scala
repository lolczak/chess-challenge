package tech.olczak.chesschallenge.chess

import scalaz.{Cord, Show}

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

  implicit val chessboardShow = new Show[Chessboard] {

    val EmptySquare = "_"

    val Separator = " "

    val EndOfRank = "\n"

    override def show(chessboard: Chessboard): Cord = {
      chessboard.board.allSquares.foldLeft(Cord.empty) {
        case (cords, s@Square(rank, file)) =>
          val symbol = chessboard.placements
            .find(_.square == s)
            .map(_.piece.symbol)
            .getOrElse(EmptySquare)

          if (isEndOfRank(file, chessboard)) cords :+ Separator :+ symbol :+ EndOfRank
          else if (isBeginningOfRank(file)) cords :+ symbol
          else cords :+ Separator :+ symbol
      }
    }

    private def isBeginningOfRank(file: Int): Boolean = file == 0

    private def isEndOfRank(file: Int, chessboard: Chessboard): Boolean = file == chessboard.board.fileCount - 1

  }

}
