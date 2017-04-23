package tech.olczak.chesschallenge

import tech.olczak.chesschallenge.app.Commands._
import tech.olczak.chesschallenge.app.Environment
import tech.olczak.chesschallenge.app.cli.SimpleCliParser
import tech.olczak.chesschallenge.app.effect._
import tech.olczak.chesschallenge.solver.BacktrackingSolver

object ChessChallengeApp extends App {

  val prodEnv = Environment(args.toList, SimpleCliParser, BacktrackingSolver)

  def exec[A](command: Cmd[A]): A =
    command
      .run(prodEnv)
      .foldMap(RealConsole or RealSystem)
      .unsafePerformIO()

  exec(solverCmd)

}
