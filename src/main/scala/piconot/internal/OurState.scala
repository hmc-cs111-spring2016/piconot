package piconot.internal

import picolib.maze.Maze
import picolib.semantics._

class OurState(newStateNum: Int) {
  var rules = List[picolib.semantics.Rule]()
  var stateNum = newStateNum
  def apply(rule: OurRule): OurState = {
    var newRules = this.rules :+ evalRule(rule, stateNum)
    this.rules = newRules
    this
  }
  
  def apply(stateNum: Int): OurState = {
    this.stateNum = stateNum
    this
  }
  
  def evalRule(rule: OurRule, stateNum: Int): Rule = rule match {
    case If(surr, action, nextState) => Rule(State(stateNum.toString), surr.picoSurr, action, State(nextState.toString))
 
    case While(surr: OurSurroundings, action: MoveDirection) => {
      Rule(State(stateNum.toString), surr.picoSurr, action, State(stateNum.toString))
    }
  }
}

object OurState {
  def apply(stateNum: Int): OurState = {
    new OurState(stateNum)
  }
}