package tech.olczak.chesschallenge

import tech.olczak.chesschallenge.chess._
import tech.olczak.chesschallenge.solver.ChessConfig

import scala.language.implicitConversions

object ChessObjectMother {

  implicit def toPiecePosition(tuple: (Piece, (Int, Int))): Placement = Placement(tuple._1, Square(tuple._2._1, tuple._2._2))

  val ChessConfigWithoutPieces = ChessConfig(Board(3, 3), List.empty)

  val TinyChessConfig = ChessConfig(Board(3, 3), List(King -> 2, Rook -> 1))

  val TinyConfigSolutions = List(
    Chessboard.empty(Board(3, 3))
      .placePiece(King ->(0, 0))
      .placePiece(King ->(0, 2))
      .placePiece(Rook ->(2, 1)),

    Chessboard.empty(Board(3, 3))
      .placePiece(King ->(0, 0))
      .placePiece(Rook ->(1, 2))
      .placePiece(King ->(2, 0)),

    Chessboard.empty(Board(3, 3))
      .placePiece(King ->(0, 2))
      .placePiece(Rook ->(1, 0))
      .placePiece(King ->(2, 2)),

    Chessboard.empty(Board(3, 3))
      .placePiece(Rook ->(0, 1))
      .placePiece(King ->(2, 0))
      .placePiece(King ->(2, 2))
  )

  val MediumChessConfig = ChessConfig(Board(4, 4), List(Rook -> 2, Knight -> 4))

  val MediumConfigSolutions = List(
    Chessboard.empty(Board(4, 4))
      .placePiece(Knight ->(0, 1))
      .placePiece(Knight ->(0, 3))
      .placePiece(Rook ->(1, 2))
      .placePiece(Knight ->(2, 1))
      .placePiece(Knight ->(2, 3))
      .placePiece(Rook ->(3, 0)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Knight ->(0, 1))
      .placePiece(Knight ->(0, 3))
      .placePiece(Rook ->(1, 0))
      .placePiece(Knight ->(2, 1))
      .placePiece(Knight ->(2, 3))
      .placePiece(Rook ->(3, 2)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook ->(0, 0))
      .placePiece(Knight ->(1, 1))
      .placePiece(Knight ->(1, 3))
      .placePiece(Rook ->(2, 2))
      .placePiece(Knight ->(3, 1))
      .placePiece(Knight ->(3, 3)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook ->(0, 2))
      .placePiece(Knight ->(1, 1))
      .placePiece(Knight ->(1, 3))
      .placePiece(Rook ->(2, 0))
      .placePiece(Knight ->(3, 1))
      .placePiece(Knight ->(3, 3)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook ->(0, 1))
      .placePiece(Knight ->(1, 0))
      .placePiece(Knight ->(1, 2))
      .placePiece(Rook ->(2, 3))
      .placePiece(Knight ->(3, 0))
      .placePiece(Knight ->(3, 2)),

    Chessboard.empty(Board(4, 4))
      .placePiece(Rook ->(0, 3))
      .placePiece(Knight ->(1, 0))
      .placePiece(Knight ->(1, 2))
      .placePiece(Rook ->(2, 1))
      .placePiece(Knight ->(3, 0))
      .placePiece(Knight ->(3, 2)),


    Chessboard.empty(Board(4, 4))
      .placePiece(Knight ->(0, 0))
      .placePiece(Knight ->(0, 2))
      .placePiece(Rook ->(1, 3))
      .placePiece(Knight ->(2, 0))
      .placePiece(Knight ->(2, 2))
      .placePiece(Rook ->(3, 1)),


    Chessboard.empty(Board(4, 4))
      .placePiece(Knight ->(0, 0))
      .placePiece(Knight ->(0, 2))
      .placePiece(Rook ->(1, 1))
      .placePiece(Knight ->(2, 0))
      .placePiece(Knight ->(2, 2))
      .placePiece(Rook ->(3, 3))
  )

}
