package piconot.internal
import picolib.maze.Maze
import picolib.semantics._

class SurroundingsChecker(val status:RelativeDescription, 
      val directions:Directionlist = new Directionlist(List())) {
  def on(list:Directionlist):SurroundingsChecker = {
    new SurroundingsChecker(status, list)
  }
  
  def and(other:SurroundingsChecker):Surroundings = {
    val newSurroundings = List(North, East, West, South).map(x => {
      if (directions.directions.contains(x))
        status
      else if (other.directions.directions.contains(x))
        other.status
      else
        Anything
    })
    new Surroundings(newSurroundings(0), newSurroundings(1), newSurroundings(2), newSurroundings(3))
  }
  def and(other:Directionlist):SurroundingsChecker = {
    new SurroundingsChecker(status, this.directions and other)
  }
}

object SurroundingsChecker {
  implicit def toSurroundings(s:SurroundingsChecker):Surroundings = {
    val newSurroundings = List(North, East, West, South).map(x => {
      if (s.directions.directions.contains(x))
        s.status
      else
        Anything
    })
    new Surroundings(newSurroundings(0), newSurroundings(1), newSurroundings(2), newSurroundings(3))
  }

  def blocked = new SurroundingsChecker(Blocked)
  def open = new SurroundingsChecker(Open)
}