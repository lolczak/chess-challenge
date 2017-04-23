package tech.olczak.chesschallenge.chess

import tech.olczak.chesschallenge.app.constant.Constants._

trait Piece {

  def isThreatened(occupied: Square)(tested: Square): Boolean

  val dangerRank: Int

  val symbol: String

}

case object Queen extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    occupied.rank == tested.rank || occupied.file == tested.file ||
      Math.abs(occupied.rank - tested.rank) == Math.abs(occupied.file - tested.file)

  override val dangerRank: Int = 1

  override val symbol: String = QueenSymbol

}

case object King extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    Math.abs(occupied.rank - tested.rank) <= 1 && Math.abs(occupied.file - tested.file) <= 1

  override val dangerRank: Int = 4

  override val symbol: String = KingSymbol

}

case object Rook extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    occupied.rank == tested.rank || occupied.file == tested.file

  override val dangerRank: Int = 2

  override val symbol: String = RookSymbol

}

case object Knight extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    (Math.abs(occupied.rank - tested.rank) == 2 && Math.abs(occupied.file - tested.file) == 1) ||
      (Math.abs(occupied.rank - tested.rank) == 1 && Math.abs(occupied.file - tested.file) == 2)

  override val dangerRank: Int = 5

  override val symbol: String = KnightSymbol

}


case object Bishop extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    Math.abs(occupied.rank - tested.rank) == Math.abs(occupied.file - tested.file)

  override val dangerRank: Int = 3

  override val symbol: String = BishopSymbol

}