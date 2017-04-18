package tech.olczak.chesschallenge.chess

trait Piece {

  def isThreatened(current: Square)(other: Square): Boolean
}

case object Queen extends Piece {
  override def isThreatened(current: Square)(other: Square): Boolean = ???
}

case object King extends Piece {

  override def isThreatened(current: Square)(other: Square): Boolean =
    Math.abs(current.rank - other.rank) <= 1 && Math.abs(current.file - other.file) <= 1

}