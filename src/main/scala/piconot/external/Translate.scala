package piconot.external

import picolib.semantics._

/**
 * A class for converting AST of our external syntax into AST used by picolib
 */
object Translate {
  
  /**
   * Converts a list of StateStructs into a list of Rules
   */
  def convertAST(lst: List[StateStruct]): List[Rule] = lst.map(convertStruct).flatten
  
  /**
   * Converts a single StateStruct into a list of Rules
   */
  def convertStruct(struct: StateStruct): List[Rule] = struct.rules map { mkRule(struct.name, _) }
  
  /**
   * Makes a Rule case class from a state name and PartialRule
   */
  def mkRule(state: State, prule: PartialRule): Rule = {
    val surroundings = mkSurroundings(prule.block, prule.open)
    Rule(state, surroundings, prule.move, prule.toState)
  }
  
  /** 
   *  Exception thrown if an arrow is denoted as both blocked and open by the user
   */
  case class ArrowConflictException(msg: String) extends Exception(msg)
  
  /**
   * Makes a Surroundings case class using the list of MoveDirections for both block and open
   */
  def mkSurroundings(block: List[MoveDirection], open: List[MoveDirection]): Surroundings = {
    val surr = List(North, East, West, South) map { x =>
      (block contains x, open contains x) match {
        case (true, false)  => Blocked
        case (false, true)  => Open
        case (false, false) => Anything
        case (true,true)    => throw ArrowConflictException("ERROR: Arrow Conflict!")
      }
    }
    Surroundings(surr(0), surr(1), surr(2), surr(3))
  }
  
}