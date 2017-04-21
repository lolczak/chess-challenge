package tech.olczak.chesschallenge.chess

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

  override val symbol: String = "Q"

}

case object King extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    Math.abs(occupied.rank - tested.rank) <= 1 && Math.abs(occupied.file - tested.file) <= 1

  override val dangerRank: Int = 4

  override val symbol: String = "K"

}

case object Rook extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    occupied.rank == tested.rank || occupied.file == tested.file

  override val dangerRank: Int = 2

  override val symbol: String = "R"

}

case object Knight extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    (Math.abs(occupied.rank - tested.rank) == 2 && Math.abs(occupied.file - tested.file) == 1) ||
      (Math.abs(occupied.rank - tested.rank) == 1 && Math.abs(occupied.file - tested.file) == 2)

  override val dangerRank: Int = 5

  override val symbol: String = "N"

}


case object Bishop extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    Math.abs(occupied.rank - tested.rank) == Math.abs(occupied.file - tested.file)

  override val dangerRank: Int = 3

  override val symbol: String = "B"

}