package piconot.external.parser

import scala.util.parsing.combinator._
import piconot.external._
import picolib.semantics._

/**
 * Parser for external DSL
 */
object PicoParser extends JavaTokenParsers with PackratParsers {
  
  /**
   * Allows for parsing comments
   */
  override protected val whiteSpace = """(\s|#.*)+""".r
  
  /**
   * Applies the parser to a string
   */
  def apply(s: String): ParseResult[List[StateStruct]] = parseAll(program,s)
  
  /**
   * A program can be made of any number of StateStructs
   */
  def program: PackratParser[List[StateStruct]] = struct*
  
  /**
   * A StateStruct has the state name and the a list of PRules
   */
  def struct: PackratParser[StateStruct] =
    "State" ~ state ~ ":" ~ prules ^^
      {case "State" ~ n ~ ":" ~ r => StateStruct(n,r)}
  
  /**
   * A list of PRules is made of one or more PRule
   */
  def prules: PackratParser[List[PartialRule]] = prule*
  
  /**
   * A prule is a comma separated blocked and opened arrows followed by a '=>' followed by comma separated
   *  move direction and next state
   */
  def prule: PackratParser[PartialRule] =
    arrows ~ "," ~ arrows ~ "=>" ~ arrow ~ "," ~ state ^^
      {case b ~ "," ~ o ~ "=>" ~ m ~ "," ~ s => PartialRule(b,o,m,s)}
  
  /**
   * Any number of arrows mapped to MoveDirections
   */
  def arrows: PackratParser[List[MoveDirection]] = arrow*
  
  /**
   * Maps an arrow to a MoveDirection
   */
  def arrow: PackratParser[MoveDirection] = (
        ("↑" ^^^ North)
      | ("→" ^^^ East)
      | ("←" ^^^ West)
      | ("↓" ^^^ South)
      | ("." ^^^ StayHere)
   )
   
  /**
   * Parses a state name
   */
  def state: PackratParser[State] = """\d\d?""".r ^^ State
}