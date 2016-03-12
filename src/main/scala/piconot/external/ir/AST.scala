package piconot.external.PicoDSL.ir

import picolib.semantics._

// each program is a bunch of state blocks
case class ProgramAST(states: List[StateBlockAST])

// each state block has a name for the state and rules
case class StateBlockAST(name: String, rules: List[RuleAST])

// each rule has map conditions that much be met, a movement direction, and the name of a state to transition to
case class RuleAST(conditions: List[ConditionAST], direction: MoveDirection, stateName: String)

// conditions require that you are either able to move or not move in a certain direction
case class ConditionAST(open: Boolean, direction: MoveDirection)