package tech.olczak.chesschallenge.app

import tech.olczak.chesschallenge.app.cli.CliParser
import tech.olczak.chesschallenge.solver.ChessChallengeSolver

case class Environment(args: List[String],
                       cliParser: CliParser,
                       solver: ChessChallengeSolver)
