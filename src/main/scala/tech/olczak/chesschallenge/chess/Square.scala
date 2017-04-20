package tech.olczak.chesschallenge.chess

case class Square(rank: Int, file: Int) extends Ordered[Square] { self =>

  override def compare(that: Square): Int = {
    val rankDiff = self.rank - that.rank
    if (rankDiff != 0) rankDiff
    else self.file - that.file
  }

}
