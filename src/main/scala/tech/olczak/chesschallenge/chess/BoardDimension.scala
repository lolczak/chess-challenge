package tech.olczak.chesschallenge.chess

case class BoardDimension(rankCount: Int, fileCount: Int) {

  lazy val allSquares =
    (for (rank <- 0 until rankCount; file <- 0 until fileCount) yield Square(rank, file)).toList

}
