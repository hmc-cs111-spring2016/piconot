package piconot.internal
import picolib.maze.Maze
import picolib.semantics._
import RuleListWrapper._
import Directionlist._

class RuleWrapper(val thisState:State = State(RuleWrapper.counter.toString),
                  val surroundings:Surroundings = Surroundings(Anything, Anything, Anything, Anything),
                  val dir:MoveDirection = North, 
                  val endState:State = State((RuleWrapper.counter + 1).toString)) {
  def this(direction: Directionlist) = {
    this(dir = direction.directions(0))
  } 
  
  def this(direction: Directionlist, end:State) = {
    this(dir = direction.directions(0), endState = end)
  }
  
  RuleWrapper.counter += 2
  
  def until(newSurroundings:Surroundings):RuleListWrapper = {
    val transitionRule = new RuleWrapper(thisState, newSurroundings, StayHere, endState)
    val thisRule = new RuleWrapper(thisState, 
                                   Surroundings(Anything, Anything, Anything, Anything), 
                                   dir, 
                                   thisState)
    new RuleListWrapper(List(transitionRule, thisRule))
  }
  
  
}

object RuleWrapper {
  implicit def toRule(r:RuleWrapper):Rule = {
    Rule(r.thisState, r.surroundings, r.dir, r.endState)
  }
  implicit def toRuleListWrapper(r:RuleWrapper):RuleListWrapper = {
    new RuleListWrapper(List(r))
  }
  def go(direction:Directionlist) = {
    if (direction.directions.head != StayHere) {
      new RuleWrapper(direction)
    } else {
      new RuleWrapper(direction, loopState)
    }
  }
  

  var counter:Int = 0
}