package tech.olczak.chesschallenge

import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.implicitConversions

object ChessObjectMother {

  val ChessConfigWithoutPieces = ChessConfig(Board(3, 3), List.empty)

  val TinyChessConfig = ChessConfig(Board(3, 3), List(King -> 2, Rook -> 1))

  val TinyConfigSolutions = List(
    Chessboard.empty(Board(3, 3))
      .placePiece(King, Square(0, 0))
      .placePiece(King, Square(0, 2))
      .placePiece(Rook, Square(2, 1)),

    Chessboard.empty(Board(3, 3))
      .placePiece(King, Square(0, 0))
      .placePiece(Rook, Square(1, 2))
      .placePiece(King, Square(2, 0)),

    Chessboard.empty(Board(3, 3))
      .placePiece(King, Square(0, 2))
      .placePiece(Rook, Square(1, 0))
      .placePiece(King, Square(2, 2)),

    Chessboard.empty(Board(3, 3))
      .placePiece(Rook, Square(0, 1))
      .placePiece(King, Square(2, 0))
      .placePiece(King, Square(2, 2))
  )

  val MediumChessConfig = ChessConfig(Board(4, 4), List(Rook -> 2, Knight -> 4))

  val MediumConfigSolutions = List(
    Chessboard.empty(Board(4, 4))
      .placePiece(Knight, Square(0, 1))
      .placePiece(Knight, Square(0, 3))
      .placePiece(Rook, Square(1, 2))
      .placePiece(Knight, Square(2, 1))
      .placePiece(Knight, Square(2, 3))
      .placePiece(Rook, Square(3, 0)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Knight, Square(0, 1))
      .placePiece(Knight, Square(0, 3))
      .placePiece(Rook, Square(1, 0))
      .placePiece(Knight, Square(2, 1))
      .placePiece(Knight, Square(2, 3))
      .placePiece(Rook, Square(3, 2)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook, Square(0, 0))
      .placePiece(Knight, Square(1, 1))
      .placePiece(Knight, Square(1, 3))
      .placePiece(Rook, Square(2, 2))
      .placePiece(Knight, Square(3, 1))
      .placePiece(Knight, Square(3, 3)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook, Square(0, 2))
      .placePiece(Knight, Square(1, 1))
      .placePiece(Knight, Square(1, 3))
      .placePiece(Rook, Square(2, 0))
      .placePiece(Knight, Square(3, 1))
      .placePiece(Knight, Square(3, 3)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook, Square(0, 1))
      .placePiece(Knight, Square(1, 0))
      .placePiece(Knight, Square(1, 2))
      .placePiece(Rook, Square(2, 3))
      .placePiece(Knight, Square(3, 0))
      .placePiece(Knight, Square(3, 2)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook, Square(0, 3))
      .placePiece(Knight, Square(1, 0))
      .placePiece(Knight, Square(1, 2))
      .placePiece(Rook, Square(2, 1))
      .placePiece(Knight, Square(3, 0))
      .placePiece(Knight, Square(3, 2)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Knight, Square(0, 0))
      .placePiece(Knight, Square(0, 2))
      .placePiece(Rook, Square(1, 3))
      .placePiece(Knight, Square(2, 0))
      .placePiece(Knight, Square(2, 2))
      .placePiece(Rook, Square(3, 1)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Knight, Square(0, 0))
      .placePiece(Knight, Square(0, 2))
      .placePiece(Rook, Square(1, 1))
      .placePiece(Knight, Square(2, 0))
      .placePiece(Knight, Square(2, 2))
      .placePiece(Rook, Square(3, 3))
  )

}
