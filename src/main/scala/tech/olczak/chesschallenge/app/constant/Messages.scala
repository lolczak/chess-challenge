package tech.olczak.chesschallenge.app.constant

object Messages {

  val GreetingMsg = "Hello, starting chess challenge app..."

  val InvalidArgumentsMsg = "Invalid arguments: %s"

  val UsageMsg = "Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\""

  val SolverStartMsg = "Solving chess challenge for %s and %s"

  val FoundSolutionsMsg = "Found %d solutions."

  val ElapsedTimeMsg = "Elapsed time: %d sec and %d millis."

  val PieceDuplicationMsg = "piece duplication not allowed"

  val InvalidRankCountMsg = "rank count must be a number"

  val InvalidFileCountMsg = "file count must be a number"

  val UnknownPieceMsg = "%s unknown piece symbol"

  val PieceGroupFormatFailureMsg = "wrong format of piece group: %s"

  val NumberExpectedMsg = "%s is not a number"

  val TooLessArgumentsMsg = "minimum 2 arguments expected"

}
