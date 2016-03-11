package piconot.external.PicoDSL.interpreter

import piconot.external.PicoDSL.ir._
import picolib.semantics._


package object semantics {
  var numStates = 0
  val stateToInt = collection.mutable.Map[String,String]()
  
  def eval(program: ProgramAST): List[Rule] = {
    program.states.foreach((s: StateBlockAST) => {stateToInt(s.name) = numStates.toString; numStates += 1})
    program.states.map((s: StateBlockAST) => handleState(s)).flatten
  }
  
  def handleState(s: StateBlockAST): List[Rule] = {
    s.rules.map((r: RuleAST) => Rule(State(stateToInt(s.name)), condToSurr(r.conditions), r.direction, State(stateToInt(r.stateName))))
  }
  
  def condToSurr(conditions: List[ConditionAST]): Surroundings = {
    var north: RelativeDescription = Anything
    var south: RelativeDescription = Anything
    var east:  RelativeDescription = Anything
    var west:  RelativeDescription = Anything
    
    conditions.foreach { cond: ConditionAST =>
      cond.direction match {
        case North => if (cond.open) {north = Open} else {north = Blocked}
        case South => if (cond.open) {south = Open} else {south = Blocked}
        case East => if (cond.open) {east = Open} else {east = Blocked}
        case West => if (cond.open) {west = Open} else {west = Blocked}
        case StayHere => {}
      }
    }
    
    Surroundings(north, east, west, south)
  }
}