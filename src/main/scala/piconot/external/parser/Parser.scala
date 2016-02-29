package piconot.external.parser

import scala.language.postfixOps
import scala.util.parsing.combinator.RegexParsers

import picolib.semantics._

  /*
   * Syntax for a valid Picobot rule:
   * 
   *          rule ::= state pattern "->" direction state
   *       pattern ::= ndirection edirection wdirection sdirection
   *    ndirection ::= "N" | odirection
   *    edirection ::= "E" | odirection
   *    wdirection ::= "N" | odirection
   *    sdirection ::= "S" | odirection
   *    odirection ::= "*" | "x" 
   *     direction ::= "N" | "E" | "W" | "S" | "x"
   *         state ::= digit{digit}
   * 
   */

object PiconotParser extends RegexParsers {

  // for parsing comments
  override protected val whiteSpace = """(\s|#.*)+""".r
  
  // parsing interface
  def apply(s: String): ParseResult[List[Rule]] = parseAll(program, s)
    
  def program: Parser[List[Rule]] = rule*
  
  def rule: Parser[Rule] = 
    state ~ pattern ~ "->" ~ direction ~ state ^^
    { case stateNow ~ surroundings ~ "->" ~ moveDirection ~ newState ⇒
        Rule(stateNow, surroundings, moveDirection, newState) }

  def pattern: Parser[Surroundings] =
    ndirection ~ edirection ~ wdirection ~ sdirection ^^
      { case n ~ e ~ w ~ s ⇒ Surroundings(n, e, w, s) }
  
  def ndirection: Parser[RelativeDescription] = 
    (   ("N" ^^^ Blocked) 
      | odirection 
      | failure("expected N, or *, or x") )
  
  def edirection: Parser[RelativeDescription] = 
    (   ("E" ^^^ Blocked) 
      | odirection 
      | failure("expected E, or *, or x") )
  
  def wdirection: Parser[RelativeDescription] = 
    (   ("W" ^^^ Blocked) 
      | odirection 
      | failure("expected W, or *, or x") )
      
  def sdirection: Parser[RelativeDescription] = 
    (   ("S" ^^^ Blocked) 
      | odirection 
      | failure("expected S, or *, or x") )
  
  def odirection: Parser[RelativeDescription] = 
    (   ("*" ^^^ Anything) 
      | ("x" ^^^ Open) )
  
  def direction: Parser[MoveDirection] =
    (   ("N" ^^^ North)
      | ("E" ^^^ East)
      | ("W" ^^^ West)
      | ("S" ^^^ South)
      | ("X" ^^^ StayHere) )
  
  def state: Parser[State] = 
    """\d\d?""".r ^^ State
}
