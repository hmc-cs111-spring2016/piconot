package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

abstract class OurRule {
  def while(surr: OurSurroundings, action: OurAction): OurRule = 0
  def if(surr: OurSurroundings, action: OurAction, state: OurState): Our Rule = 0
}
