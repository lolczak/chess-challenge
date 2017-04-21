package tech.olczak.chesschallenge.app.cli

import org.scalatest.{Matchers, FlatSpec}

import scalaz.-\/

class SimpleCliParserSpec extends FlatSpec with Matchers {

  "A cli parser" should "report an error when there is insufficient number of args" in {
    SimpleCliParser.parse(List.empty) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("3")) should matchPattern { case -\/(ParseFailure(_)) => }
    SimpleCliParser.parse(List("4")) should matchPattern { case -\/(ParseFailure(_)) => }
  }

}
