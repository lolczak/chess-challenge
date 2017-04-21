package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.chess.{Piece, Board}

case class ChessProblem(board: Board, pieceGroups: List[(Piece, Int)])
