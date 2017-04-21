package tech.olczak.chesschallenge

import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.implicitConversions

object ChessObjectMother {

  implicit def toPiecePosition(tuple: (Piece, (Int, Int))): Placement = Placement(tuple._1, Square(tuple._2._1, tuple._2._2))

  val ChessConfigWithoutPieces = ChessConfig(Board(3, 3), List.empty)

  val TinyChessConfig = ChessConfig(Board(3, 3), List(King -> 2, Rook -> 1))

  val TinyConfigSolutions = List(
    Chessboard(Board(3, 3), Set(King ->(0, 0), King ->(0, 2), Rook ->(2, 1))),
    Chessboard(Board(3, 3), Set(King ->(0, 0), Rook ->(1, 2), King ->(2, 0))),
    Chessboard(Board(3, 3), Set(King ->(0, 2), Rook ->(1, 0), King ->(2, 2))),
    Chessboard(Board(3, 3), Set(Rook ->(0, 1), King ->(2, 0), King ->(2, 2)))
  )

  val MediumChessConfig = ChessConfig(Board(4, 4), List(Rook -> 2, Knight -> 4))

  val MediumConfigSolutions = List(
    Chessboard(Board(4, 4), Set(Knight ->(0, 1), Knight ->(0, 3), Rook ->(1, 2), Knight ->(2, 1), Knight ->(2, 3), Rook ->(3, 0))),
    Chessboard(Board(4, 4), Set(Knight ->(0, 1), Knight ->(0, 3), Rook ->(1, 0), Knight ->(2, 1), Knight ->(2, 3), Rook ->(3, 2))),
    Chessboard(Board(4, 4), Set(Rook ->(0, 0), Knight ->(1, 1), Knight ->(1, 3), Rook ->(2, 2), Knight ->(3, 1), Knight ->(3, 3))),
    Chessboard(Board(4, 4), Set(Rook ->(0, 2), Knight ->(1, 1), Knight ->(1, 3), Rook ->(2, 0), Knight ->(3, 1), Knight ->(3, 3))),
    Chessboard(Board(4, 4), Set(Rook ->(0, 1), Knight ->(1, 0), Knight ->(1, 2), Rook ->(2, 3), Knight ->(3, 0), Knight ->(3, 2))),
    Chessboard(Board(4, 4), Set(Rook ->(0, 3), Knight ->(1, 0), Knight ->(1, 2), Rook ->(2, 1), Knight ->(3, 0), Knight ->(3, 2))),
    Chessboard(Board(4, 4), Set(Knight ->(0, 0), Knight ->(0, 2), Rook ->(1, 3), Knight ->(2, 0), Knight ->(2, 2), Rook ->(3, 1))),
    Chessboard(Board(4, 4), Set(Knight ->(0, 0), Knight ->(0, 2), Rook ->(1, 1), Knight ->(2, 0), Knight ->(2, 2), Rook ->(3, 3)))
  )

}
