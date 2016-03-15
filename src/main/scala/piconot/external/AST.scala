package piconot.external

import picolib.semantics._

/**
 * Case class for containing a state name and a list of partial rules. Each StateStruct can represent multiple Rules
 * with the same original name
 */
case class StateStruct(name: State, rules: List[PartialRule])

/**
 * Case class for containing the specifics of a rule, barring the state it actually belongs to. A given StateStruct
 * contains a list these.
 */
case class PartialRule(block: List[MoveDirection], open: List[MoveDirection], move: MoveDirection, toState: State)
