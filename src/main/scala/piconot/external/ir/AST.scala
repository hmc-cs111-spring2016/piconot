package piconot.external.ir

sealed abstract class OurProgram
sealed abstract class OurState
sealed abstract class OurRules
sealed abstract class OurRule
sealed abstract class OurAction
sealed abstract class OurSurroundings

case class EmptyProgram() extends OurProgram
case class CurrProgram(state: OurState, states: OurProgram) extends OurProgram

case class EmptyState() extends OurState
case class CurrState(num: Int, rules: OurRules) extends OurState

case class EmptyRules() extends OurRules
case class RuleList(firstRule: OurRule, restRules: OurRules) extends OurRules

case class WhileRule(surr: OurSurroundings, action: OurAction) extends OurRule
case class IfRule(surr: OurSurroundings, action: OurAction, state: Int) extends OurRule

case class MoveN() extends OurAction
case class MoveE() extends OurAction
case class MoveW() extends OurAction
case class MoveS() extends OurAction
case class MoveX() extends OurAction

case class SurrEmpty() extends OurSurroundings
case class SurrN(surr: OurSurroundings) extends OurSurroundings
case class SurrE(surr: OurSurroundings) extends OurSurroundings
case class SurrW(surr: OurSurroundings) extends OurSurroundings
case class SurrS(surr: OurSurroundings) extends OurSurroundings
case class SurrNotN(surr: OurSurroundings) extends OurSurroundings
case class SurrNotE(surr: OurSurroundings) extends OurSurroundings
case class SurrNotW(surr: OurSurroundings) extends OurSurroundings
case class SurrNotS(surr: OurSurroundings) extends OurSurroundings
