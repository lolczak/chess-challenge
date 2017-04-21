package tech.olczak.chesschallenge.app

import org.scalatest.{Matchers, WordSpec}

import scalaz._

class ChessChallengeAppSpec extends WordSpec with Matchers {

  "A chess challenge app" when {
    "starting" should {
      "print greeting" in new TestContext {
        //when
        run(ChessChallengeApp.mainCmd)
        //then
        stdout should contain("Hello, starting chess challenge app...")
      }
    }

  }

  trait TestContext {

    var buffer = Buffer(List.empty)

    def run[A](cmd: ChessChallengeApp.Cmd[A]): A = {
      val (buf, result) = cmd.foldMap[State[Buffer, ?]](ConsoleToState).run(Buffer(List.empty))
      buffer = buf
      result
    }

    def stdout: List[String] = buffer.stdout.reverse

  }

}
