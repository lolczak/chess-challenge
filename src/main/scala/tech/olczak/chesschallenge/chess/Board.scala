package tech.olczak.chesschallenge.chess

case class Board(rankCount: Int, fileCount: Int) {

  lazy val allSquares =
    (for (rank <- 0 until rankCount; file <- 0 until fileCount) yield Square(rank, file)).toList

}
