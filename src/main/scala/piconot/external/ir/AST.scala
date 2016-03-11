package piconot.external.PicoDSL.ir

import picolib.semantics._

case class ProgramAST(states: List[StateBlockAST])

case class StateBlockAST(name: String, rules: List[RuleAST])
case class RuleAST(conditions: List[ConditionAST], direction: MoveDirection, stateName: String)
case class ConditionAST(open: Boolean, direction: MoveDirection)