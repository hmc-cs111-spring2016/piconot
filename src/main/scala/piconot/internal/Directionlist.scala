package piconot.internal
import picolib.maze.Maze
import picolib.semantics._

class Directionlist(val directions: List[MoveDirection]) {
  def and(other:Directionlist):Directionlist = {
    new Directionlist(directions ++ other.directions)
  }
}

object Directionlist {
  def up = new Directionlist(List(North))
  def down = new Directionlist(List(South))
  def left = new Directionlist(List(West))
  def right = new Directionlist(List(East))
  def back = new Directionlist(List(StayHere))
}