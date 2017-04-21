package tech.olczak.chesschallenge.app.cli

import org.scalatest.{Matchers, FlatSpec}

import scalaz.-\/

class SimpleCliParserSpec extends FlatSpec with Matchers {

  "A cli parser" should "report an error when there is insufficient number of args" in {
    SimpleCliParser.parse(List.empty) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "4")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when rank count is not number" in {
    SimpleCliParser.parse(List("a", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("##@", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List(":", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("122a", "3", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when file count is not number" in {
    SimpleCliParser.parse(List("4", "a", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("5", "##@", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", ":", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "122a", "K2")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

  it should "fail when piece group is incorrect" in {
    SimpleCliParser.parse(List("4", "4", "K")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("5", "5", "Q")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "7", "2K")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3", "8", "3243")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

}
