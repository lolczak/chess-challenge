package tech.olczak.chesschallenge.solver

import tech.olczak.chesschallenge.chess.{Board, Piece}

case class ChessConfig(board: Board, pieceGroups: List[(Piece, Int)])
