package piconot.external.semantics

import picolib.semantics._
import piconot.external.ir._

package object semantics {
  def eval(prgm: OurProgram): List[Rule] = prgm match {
    case EmptyProgram() => List[Rule]()
    case CurrProgram(initState: OurState, program: OurProgram) ⇒ evalState(initState) ++ eval(program)
  }
  
  def evalState(state: OurState): List[Rule] = state match {
    case EmptyState() => List[Rule]()
    case CurrState(i, rules) ⇒ evalRules(i,rules)
  }
  
  def evalRules(currState: Int, rules: OurRules): List[Rule] = rules match {
    case EmptyRules() => List[Rule]()
    case RuleList(firstRule, restRules) => evalRule(currState, firstRule) :: evalRules(currState, restRules)
  }
  
  def evalRule(i: Int, rule: OurRule): Rule = rule match {
    case WhileRule(surr, action) => Rule(State(i.toString()), evalSurr(surr), evalAction(action), State(i.toString()))
    case IfRule(surr, action, state) => Rule(State(i.toString()), evalSurr(surr), evalAction(action), State(state.toString()))
  }
  
  def evalAction(act: OurAction): MoveDirection = act match {
    case MoveN() => North
    case MoveE() => East
    case MoveW() => West
    case MoveS() => South
    case MoveX() => StayHere
  }
  
  def evalSurr(surr: OurSurroundings): Surroundings = surr match {
    case SurrEmpty() => {
      Surroundings(Anything, Anything, Anything, Anything)
    }  
    case SurrN(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(Blocked, nextSurr.east, nextSurr.west, nextSurr.south)
    }
    case SurrE(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, Blocked, nextSurr.west, nextSurr.south)
    }
    case SurrW(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, nextSurr.east, Blocked, nextSurr.south)
    }
    case SurrS(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, nextSurr.east, nextSurr.west, Blocked)
    }
    case SurrNotN(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(Open, nextSurr.east, nextSurr.west, nextSurr.south)
    }
    case SurrNotE(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, Open, nextSurr.west, nextSurr.south)
    }
    case SurrNotW(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, nextSurr.east, Open, nextSurr.south)
    }
    case SurrNotS(currentSurr) => {
      var nextSurr = evalSurr(currentSurr)
      Surroundings(nextSurr.north, nextSurr.east, nextSurr.west, Open)
    }
  }
}
