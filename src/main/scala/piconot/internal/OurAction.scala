package piconot.internal


import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

class OurAction

object OurAction {
  def move(dir: MoveDirection): MoveDirection = dir
}
//    case Directions.North => North
//    case Directions.East => East
//    case Directions.West => West
//    case Directions.South => South
//  }

//
//object Directions extends Enumeration {
//  val North,East,West,South = Value
//}