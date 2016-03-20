package piconot.external.parser

import scala.language.postfixOps
import scala.util.parsing.combinator.RegexParsers

import picolib.semantics._

/*
 * rule ::= "(" state ("," surrounding | "") (";" (go | go "," newstate) | "") ")"
 * state ::= ("s" | "S") "tate:" digit{digit}
 * surrounding ::= onewall | n "," noN | e "," noE | w "," noW | s "," noS
 * onewall ::= n | e | w | s
 * n ::= ("n" | "N") ("" | "orth") ":" wall
 * e ::= ("e" | "E") ("" | "ast") ":" wall
 * w ::= ("w" | "W") ("" | "est") ":" wall
 * s ::= ("s" | "S") ("" | "outh") ":" wall
 * wall ::= "*" | "x" | "+"
 * noN ::= e | w | s | e "," noNE | w "," noNW | s "," noNS
 * noE ::= n | w | s | n "," noNE | w "," noEW | s "," noES
 * noW ::= n | e | s | n "," noNW | e "," noEW | s "," noWS
 * noS ::= n | e | w | n "," noNS | e "," noES | w "," noWS
 * noNE ::= w | s | w "," s | s "," w
 * noNW ::= e | s | e "," s | s "," e
 * noNS ::= e | w | e "," w | w "," e
 * noEW ::= n | s | n "," s | s "," n
 * noES ::= n | w | n "," w | w "," n
 * noWS ::= n | e | n "," e | e "," n
 * go ::= ("g" | "G") "o:" direction
 * direction ::= goN | goE | goW | goS | ("x" | "X")
 * goN ::= ("n" | "N") ("" | "orth")
 * goE ::= ("e" | "E") ("" | "ast")
 * goW ::= ("w" | "W") ("" | "est")
 * goS ::= ("s" | "S") ("" | "outh")
 * newState ::= ("n" | "N") "ew" state
 */

object PiconotParser extends RegexParsers {

  override protected val whiteSpace = """(\s|#.*)+""".r

  def apply(s: String): ParseResult[List[Rule]] = parseAll(program, s)

  def program: Parser[List[Rule]] = rule*

  def defaultWalls: Surroundings = 
    Surroundings(Anything, Anything, Anything, Anything)

  def defaultMove: MoveDirection = StayHere

  def rule: Parser[Rule] = 
    "("~state~("" | ","~surrounding)~("" | ";"~go~(""|","~newState))~")" ^^ 
    {
      case "("~stateNow~""~""~")" => 
        Rule(stateNow, defaultWalls, defaultMove, stateNow)

      case "("~stateNow~","~walls~""~")" =>
        Rule(stateNow, walls, defaultMove, stateNow)

      case "("~stateNow~""~";"~move~""~")" =>
        Rule(stateNow, defaultWalls, move, stateNow)

      case "("~stateNow~""~";"~move~","~stateAfter~")" =>
        Rule(stateNow, defaultWalls, move, stateAfter)

      case "("~stateNow~","~walls~";"~move~","~stateAfter~")" =>
        Rule(stateNow, walls, move, stateAfter)
    }


  def surrounding: Parser[Surroundings] = 
  (onewall | n~","~noN | e~","~noE | w~","~noW | s~","~noS) ^^ {
    case n => Surroundings(n, Anything, Anything, Anything)
    case e => Surroundings(Anything, e, Anything, Anything)
    case w => Surroundings(Anything, Anything, w, Anything)
    case s => Surroundings(Anything, Anything, Anything, s)
    
    case n~","~noN => noN(n)
    case e~","~noE => noE(e)
    case w~","~noW => noW(w)
    case s~","~noS => noS(s)
  }

  def noN: Parser[RelativeDescription => Surroundings] = 
    (e | w | s | e~","~noNE | w~","~noNW | s~","~noNS) ^^
  {
    case e => Surroundings(_:RelativeDescription, e, Anything, Anything)
    case w => Surroundings(_:RelativeDescription, Anything, w, Anything)
    case s => Surroundings(_:RelativeDescription, Anything, Anything, s)

    case e~","~noNE => noNE(east=e)
    case w~","~noNW => noNW(west=w)
    case s~","~noNS => noNS(south=s)
  }

  def noE: Parser[RelativeDescription => Surroundings] = 
    (n | w | s | n~","~noNE | w~","~noEW | s~","~noES) ^^
  {
    case n => Surroundings(n, _:RelativeDescription, Anything, Anything)
    case w => Surroundings(Anything, _:RelativeDescription, w, Anything)
    case s => Surroundings(Anything, _:RelativeDescription, Anything, s)

    case n~","~noNE => noNE(north=n)
    case w~","~noEW => noEW(west=w)
    case s~","~noES => noES(south=s)
  }

  def noW: Parser[RelativeDescription => Surroundings] = 
    (n | e | s | n~","~noNW | e~","~noEW | s~","~noWS) ^^
  {
    case n => Surroundings(n, Anything, _:RelativeDescription, Anything)
    case e => Surroundings(Anything, e, _:RelativeDescription, Anything)
    case s => Surroundings(Anything, Anything, _:RelativeDescription, s)

    case n~","~noNW => noNW(north=n)
    case e~","~noEW => noEW(east=e)
    case s~","~noWS => noWS(south=s)
  }

  def noS: Parser[RelativeDescription => Surroundings] = 
    (n | e | w | n~","~noNS | e~","~noES | w~","~noWS) ^^
  {
    case n => Surroundings(n, Anything, Anything, _:RelativeDescription)
    case e => Surroundings(Anything, e, Anything, _:RelativeDescription)
    case w => Surroundings(Anything, Anything, w,  _:RelativeDescription)

    case n~","~noNS => noNS(north=n)
    case e~","~noES => noES(east=e)
    case w~","~noWS => noWS(west=w)
  }

  def noNE: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (w | s | w~","~s | s~","~w) ^^
  {
    case w => Surroundings(_:RelativeDescription, _:RelativeDescription, w, Anything)
    case s => Surroundings(_:RelativeDescription, _:RelativeDescription, Anything, s)
    case w~","~s | s~","~w =>
      Surroundings(_:RelativeDescription, _:RelativeDescription, w, s)
  }

  def noNW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (e | s | e~","~s | s~","~e) ^^
  {
    case e => Surroundings(_:RelativeDescription, e, _:RelativeDescription, Anything)
    case s => Surroundings(_:RelativeDescription, _:RelativeDescription, Anything, s)
    case e~","~s | s~","~e =>
      Surroundings(_:RelativeDescription, e, _:RelativeDescription, s)
  }

  def noNS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (e | w | e~","~w | w~","~e) ^^
  {
    case e => Surroundings(_:RelativeDescription, e, Anything, _:RelativeDescription)
    case w => Surroundings(_:RelativeDescription, Anything, w,_:RelativeDescription)
    case e~","~w | w~","~e =>
      Surroundings(_:RelativeDescription, e, w, _:RelativeDescription)
  }

  def noEW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (n | s | n~","~s | s~","~n) ^^
  {
    case n => Surroundings(n, _:RelativeDescription, _:RelativeDescription, Anything)
    case s => Surroundings(Anything, _:RelativeDescription, _:RelativeDescription, s)
    case n~","~s | s~","~n =>
      Surroundings(n, _:RelativeDescription, _:RelativeDescription, s)
  }

  def noES: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (n | w | n~","~w | w~","~n) ^^
  {
    case n => Surroundings(n, _:RelativeDescription, Anything, _:RelativeDescription)
    case w => Surroundings(Anything, _:RelativeDescription, w, _:RelativeDescription)
    case n~","~w | w~","~n =>
      Surroundings(n, _:RelativeDescription, w, _:RelativeDescription)
  }

  def noWS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
    (n | e | n~","~e | e~","~n) ^^
  {
    case n => Surroundings(n, Anything, _:RelativeDescription, _:RelativeDescription)
    case e => Surroundings(Anything, e, _:RelativeDescription, _:RelativeDescription)
    case n~","~e | e~","~n =>
      Surroundings(n, e, _:RelativeDescription, _:RelativeDescription)
  }

  def onewall: Parser[RelativeDescription] = (n | e | w | s)
    
  def n: Parser[RelativeDescription] = 
    (   ("n" | "N")~("" | "orth")~":"~>wall
      | failure("expected *, x, or + after tag N(orth):") )

  def e: Parser[RelativeDescription] = 
    (   ("e" | "E")~("" | "ast")~":"~>wall
      | failure("expected *, x, or + after tag E(ast):") )

  def w: Parser[RelativeDescription] = 
    (   ("w" | "W")~("" | "est")~":"~>wall
      | failure("expected *, x, or + after tag W(est):") )

  def s: Parser[RelativeDescription] = 
    (   ("s" | "S")~("" | "outh")~":"~>wall
      | failure("expected *, x, or + after tag S(outh):") )

  def wall: Parser[RelativeDescription] = 
    (   ("*" ^^^ Anything)
      | ("x" ^^^ Open)
      | ("+" ^^^ Blocked) )

  def go: Parser[MoveDirection] = ("g" | "G")~"o:"~>direction

  def direction: Parser[MoveDirection] =
    (   (("x"|"X") ^^^ StayHere)
      | (("n" | "N")~("" | "orth") ^^^ North)
      | (("e" | "E")~("" | "ast") ^^^ East)
      | (("w" | "W")~("" | "est") ^^^ West)
      | (("s" | "S")~("" | "outh") ^^^ South) )

  def state: Parser[State] =
    (("s"|"S")~"tate:"~>"""\d\d?""".r) ^^ State

  def newState: Parser[State] =
    ("n"|"N")~"ew"~>state
}
