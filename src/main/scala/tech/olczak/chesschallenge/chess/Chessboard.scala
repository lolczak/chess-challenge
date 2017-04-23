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

  def empty(board: Board) = Chessboard(board, List.empty, board.allSquares)

  implicit val chessboardShow = new Show[Chessboard] {

    val EmptySquare = "_"

    val Separator = " "

    val EndOfLine = "\n"

    override def show(chessboard: Chessboard): Cord = {
      val LastFile = chessboard.board.fileCount - 1
      val symbolFor = getSymbolFor(chessboard)(_)

      chessboard.board.allSquares.foldLeft(Cord.empty) {
        case (cords, square@Square(_, 0))        => cords :+ symbolFor(square)
        case (cords, square@Square(_, LastFile)) => cords :+ s"$Separator${symbolFor(square)}$EndOfLine"
        case (cords, square@Square(rank, file))  => cords :+ s"$Separator${symbolFor(square)}"
      }
    }

    private def getSymbolFor(chessboard: Chessboard)(square: Square) =
      chessboard.placements
        .find(_.square == square)
        .map(_.piece.symbol)
        .getOrElse(EmptySquare)

  }

}
