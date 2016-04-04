package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

abstract class OurRule {
}

object OurRule {
  def loop(surr: OurSurroundings)(action: MoveDirection): OurRule = While(surr, action)
  def cond(surr: OurSurroundings)(action: MoveDirection)(state: Int): OurRule = If(surr, action, state)
}

case class While(surr: OurSurroundings, action: MoveDirection) extends OurRule

case class If(surr: OurSurroundings, action: MoveDirection, state: Int) extends OurRule