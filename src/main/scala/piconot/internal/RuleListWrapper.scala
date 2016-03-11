package piconot.internal
import picolib.maze.Maze
import picolib.semantics._
import RuleWrapper._

class RuleListWrapper(val list:List[RuleWrapper]) {
  import RuleListWrapper._
  def then(other:RuleListWrapper):RuleListWrapper = {
    // There are two cases: if the last rule has the same start and end state
    // then it was just a "go direction" rule; otherwise it was a "go direction until" rule.
    val lastRule = list.last
    val secondLastRule = list.dropRight(1).last
    var newState:State = lastRule.endState
    if (lastRule.thisState == lastRule.endState) {
      newState = secondLastRule.endState
    }
    
      
    new RuleListWrapper(list ++ (other.list map {x:RuleWrapper => {
      if (x.thisState == x.endState) new RuleWrapper(newState,
                                         x.surroundings,
                                         x.dir,
                                         newState)
      else                           new RuleWrapper(newState,
                                         x.surroundings,
                                         x.dir,
                                         x.endState)}}))
    
  }
  def thenLoop(other:RuleListWrapper):RuleListWrapper = {
    // There are two cases: if the last rule has the same start and end state
    // then it was just a "go direction" rule; otherwise it was a "go direction until" rule.
    val lastRule = list.last
    val secondLastRule = list.dropRight(1).last
    var newState:State = lastRule.endState
    if (lastRule.thisState == lastRule.endState) {
      newState = secondLastRule.endState
    }
    loopState = newState
      
    new RuleListWrapper(list ++ (other.list map {x:RuleWrapper => {
      if (x.thisState == x.endState) new RuleWrapper(newState,
                                         x.surroundings,
                                         x.dir,
                                         newState)
      else                           new RuleWrapper(newState,
                                         x.surroundings,
                                         x.dir,
                                         x.endState)}}))
    
  }
}

object RuleListWrapper {
  implicit def toRuleList(r:RuleListWrapper):List[Rule] = {
    r.list map toRule
  }
  var loopState:State = State("-1")
//  def loop(wrapper:RuleListWrapper):RuleListWrapper = {
//    println(toRuleList(wrapper))
//    loopState = wrapper.list.head.endState
//    wrapper
//  }
}