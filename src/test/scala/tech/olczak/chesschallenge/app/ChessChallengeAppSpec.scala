package tech.olczak.chesschallenge.app

import org.scalatest.{Matchers, WordSpec}
import tech.olczak.chesschallenge.app.ChessChallengeApp.{IO, mainCmd}
import tech.olczak.chesschallenge.app.effect._
import scalaz._

class ChessChallengeAppSpec extends WordSpec with Matchers {

  "A chess challenge app" when {
    "starting" should {
      "print greeting" in new TestContext {
        //when
        run(mainCmd)
        //then
        stdout should contain("Hello, starting chess challenge app...")
      }
    }
  }

  it when {
    "there is not enough command line arguments" should {
      "fail with error code equal 1" in new TestContext {
        //given
        override lazy val env = Environment(List.empty)
        //when
        run(mainCmd)
        //then
        errorCode shouldBe 1
      }

      "print error message" in new TestContext {
        //given
        override lazy val env = Environment(List.empty)
        //when
        run(mainCmd)
        //then
        stderr should contain("Invalid arguments.")
        stderr should contain("Usage: sbt \"run [ranks] [files] [<piece symbol><piece count>...]\"")
      }
    }
  }

  trait TestContext {

    lazy val env = Environment(List("3", "3", "K2", "R1"))

    var buffer = Buffer(List.empty)

    def run[A](cmd: ReaderT[Free[IO, ?], Environment, A]): A = {
      val (buf, result) = cmd.run(env).foldMap(ConsoleToState or SystemToState).run(Buffer(List.empty))
      buffer = buf
      result
    }

    def stdout: List[String] = buffer.stdout.reverse

    def stderr: List[String] = buffer.stderr.reverse

    def errorCode: Int = buffer.exitCode.getOrElse(0)


  }

}
