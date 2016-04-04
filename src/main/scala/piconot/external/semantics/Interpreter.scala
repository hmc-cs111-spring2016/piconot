package piconot.external.PicoDSL.interpreter

import piconot.external.PicoDSL.ir._
import picolib.semantics._


package object semantics {
  var numStates = 0                                          // keeps track of how many states have already been used
  val nameToState = collection.mutable.Map[String,State]()   // converts state names to picolib States
  
  // adds states to the map and extracts Rules from the AST
  def eval(program: ProgramAST): List[Rule] = {
    program.states.foreach((s: StateBlockAST) => {nameToState(s.name) = State(numStates.toString); numStates += 1})
    program.states.map((s: StateBlockAST) => handleState(s)).flatten
  }
  
  // extracts Rules from a single state block 
  def handleState(s: StateBlockAST): List[Rule] = {
    s.rules.map((r: RuleAST) => Rule(nameToState(s.name), condToSurr(r.conditions), r.direction, nameToState(r.stateName)))
  }
  
  // converts between the AST representation of rule conditions and the picolib representation
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