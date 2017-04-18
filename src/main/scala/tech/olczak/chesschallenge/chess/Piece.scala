package tech.olczak.chesschallenge.chess

trait Piece {

  def isThreatened(occupied: Square)(tested: Square): Boolean

}

case object Queen extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean = ???

}

case object King extends Piece {

  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    Math.abs(occupied.rank - tested.rank) <= 1 && Math.abs(occupied.file - tested.file) <= 1

}

case object Rook extends Piece {
  override def isThreatened(occupied: Square)(tested: Square): Boolean =
    occupied.rank == tested.rank || occupied.file == tested.file
}