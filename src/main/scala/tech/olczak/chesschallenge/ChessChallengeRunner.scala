package tech.olczak.chesschallenge

import tech.olczak.chesschallenge.app.ChessChallengeApp._
import tech.olczak.chesschallenge.app.Environment
import tech.olczak.chesschallenge.app.cli.SimpleCliParser
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.solver.BacktrackingSolver


object ChessChallengeRunner extends App {

  val prodEnv = Environment(args.toList, SimpleCliParser, BacktrackingSolver)

  def unsafeRun[A](action: Action[A]): A = {
    action.run(prodEnv).foldMap(RealConsole or RealSystem)
  }

  unsafeRun(mainAction)

}
