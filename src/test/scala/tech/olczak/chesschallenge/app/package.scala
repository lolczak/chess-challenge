package tech.olczak.chesschallenge

import scalaz.State

package object app {

  type TestState[A] = State[Buffer, A]

}
