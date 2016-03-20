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

  // If no information about surroundings is given, default to this.
  def defaultWalls: Surroundings = 
    Surroundings(Anything, Anything, Anything, Anything)

  def defaultMove: MoveDirection = StayHere

  def rule: Parser[Rule] = 
    ("("~state~surrounding~";"~go~","~newState~")") ^^ 
    {
      case "("~stateNow~walls~";"~move~","~stateAfter~")" =>
        Rule(stateNow, walls, move, stateAfter)
    }

  // Parses the entire surroundings information
  def surrounding: Parser[Surroundings] = 
    (   ","~>nfirst 
      | ","~>efirst 
      | ","~>wfirst 
      | ","~>sfirst
      | ","~>onewall
      | ("" ^^^ defaultWalls)
    )

  // This matches rules where wall info is given for north first, followed
  // by more wall information
  def nfirst: Parser[Surroundings] = n~","~noN ^^
  { case first~","~rest => rest(first) }

  // Like nfirst, but east is given first
  def efirst: Parser[Surroundings] = e~","~noE ^^
  { case first~","~rest => rest(first) }

  // like nfirst, but west is given first
  def wfirst: Parser[Surroundings] = w~","~noW ^^
  { case first~","~rest => rest(first) }

  // like nfirst, but south is given first
  def sfirst: Parser[Surroundings] = s~","~noS ^^
  { case first~","~rest => rest(first) }

  def noN: Parser[RelativeDescription => Surroundings] = 
  (   nefirst 
    | nwfirst 
    | nsfirst 
    | (e ^^ {case e => Surroundings(_:RelativeDescription, e, Anything, Anything)})
    | (w ^^ {case w => Surroundings(_:RelativeDescription, Anything, w, Anything)})
    | (s ^^ {case s => Surroundings(_:RelativeDescription, Anything, Anything, s)})
  )

  def nefirst: Parser[RelativeDescription => Surroundings] = e~","~noNE ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def nwfirst: Parser[RelativeDescription => Surroundings] = w~","~noNW ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def nsfirst: Parser[RelativeDescription => Surroundings] = s~","~noNS ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def noE: Parser[RelativeDescription => Surroundings] = 
  (   enfirst 
    | ewfirst 
    | esfirst
    | (n ^^ {case n => Surroundings(n, _:RelativeDescription, Anything, Anything)})
    | (w ^^ {case w => Surroundings(Anything, _:RelativeDescription, w, Anything)})
    | (s ^^ {case s => Surroundings(Anything, _:RelativeDescription, Anything, s)})
  )

  def enfirst: Parser[RelativeDescription => Surroundings] = n~","~noNE ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def ewfirst: Parser[RelativeDescription => Surroundings] = w~","~noEW ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def esfirst: Parser[RelativeDescription => Surroundings] = s~","~noES ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def noW: Parser[RelativeDescription => Surroundings] = 
  (   wnfirst 
    | wefirst 
    | wsfirst 
    | (n ^^ {case n => Surroundings(n, Anything, _:RelativeDescription, Anything)})
    | (e ^^ {case e => Surroundings(Anything, e, _:RelativeDescription, Anything)})
    | (s ^^ {case s => Surroundings(Anything, Anything, _:RelativeDescription, s)})
  )

  def wnfirst: Parser[RelativeDescription => Surroundings] = n~","~noNW ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def wefirst: Parser[RelativeDescription => Surroundings] = e~","~noEW ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def wsfirst: Parser[RelativeDescription => Surroundings] = s~","~noWS ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  def noS: Parser[RelativeDescription => Surroundings] = 
  (   snfirst 
    | sefirst 
    | swfirst 
    | (n ^^ {case n => Surroundings(n, Anything, Anything, _:RelativeDescription)})
    | (e ^^ {case e => Surroundings(Anything, e, Anything, _:RelativeDescription)})
    | (w ^^ {case w => Surroundings(Anything, Anything, w, _:RelativeDescription)})
  )

  def snfirst: Parser[RelativeDescription => Surroundings] = n~","~noNS ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def sefirst: Parser[RelativeDescription => Surroundings] = e~","~noES ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def swfirst: Parser[RelativeDescription => Surroundings] = w~","~noWS ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  def noNE: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   wslast
    | swlast 
    | (w ^^ 
      {case w => Surroundings(_:RelativeDescription, _:RelativeDescription, w, Anything)})
    | (s ^^
      {case s => Surroundings(_:RelativeDescription, _:RelativeDescription, Anything, s)})
  )

  def wslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~s ^^ 
  { case w~","~s => Surroundings(_:RelativeDescription, _:RelativeDescription, w, s) }

  def swlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~w ^^ 
  { case s~","~w => Surroundings(_:RelativeDescription, _:RelativeDescription, w, s) }

  def noNW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   eslast
    | selast 
    | (e ^^ 
      {case e => Surroundings(_:RelativeDescription, e, _:RelativeDescription, Anything)})
    | (s ^^
      {case s => Surroundings(_:RelativeDescription, Anything, _:RelativeDescription, s)})
  )

  def eslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~s ^^ 
  { case e~","~s => Surroundings(_:RelativeDescription, e, _:RelativeDescription, s) }

  def selast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~e ^^ 
  { case s~","~e => Surroundings(_:RelativeDescription, e, _:RelativeDescription, s) }

  def noNS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   ewlast
    | welast
    | (e ^^ 
      {case e => Surroundings(_:RelativeDescription, e, Anything, _:RelativeDescription)})
    | (w ^^
      {case w => Surroundings(_:RelativeDescription, Anything, w, _:RelativeDescription)})
    )

  def ewlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~w ^^ 
  { case e~","~w => Surroundings(_:RelativeDescription, e, w, _:RelativeDescription) }

  def welast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~e ^^ 
  { case w~","~e => Surroundings(_:RelativeDescription, e, w, _:RelativeDescription) }

  def noEW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nslast
    | snlast
    | (n ^^ 
      {case n => Surroundings(n, _:RelativeDescription, _:RelativeDescription, Anything)})
    | (s ^^
      {case s => Surroundings(Anything, _:RelativeDescription, _:RelativeDescription, s)})
  )

  def nslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~s ^^ 
  { case n~","~s => Surroundings(n, _:RelativeDescription, _:RelativeDescription, s) }

  def snlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~n ^^ 
  { case s~","~n => Surroundings(n, _:RelativeDescription, _:RelativeDescription, s) }

  def noES: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nwlast
    | wnlast
    | (n ^^ 
      {case n => Surroundings(n, _:RelativeDescription, Anything, _:RelativeDescription)})
    | (w ^^
      {case w => Surroundings(Anything, _:RelativeDescription, w, _:RelativeDescription)})
  )

  def nwlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~w ^^ 
  { case n~","~w => Surroundings(n, _:RelativeDescription, w, _:RelativeDescription) }

  def wnlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~n ^^ 
  { case w~","~n => Surroundings(n, _:RelativeDescription, w, _:RelativeDescription) }

  def noWS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nelast
    | enlast
    | (n ^^ 
      {case n => Surroundings(n, Anything, _:RelativeDescription, _:RelativeDescription)})
    | (e ^^
      {case e => Surroundings(Anything, e, _:RelativeDescription, _:RelativeDescription)})
  )

  def nelast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~e ^^ 
  { case n~","~e => Surroundings(n, e, _:RelativeDescription, _:RelativeDescription) }

  def enlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~n ^^ 
  { case e~","~n => Surroundings(n, e, _:RelativeDescription, _:RelativeDescription) }


  def onewall: Parser[Surroundings] = 
    (   (n ^^ {case north => Surroundings(north, Anything, Anything, Anything)})
      | (e ^^ {case east => Surroundings(Anything, east, Anything, Anything)})
      | (w ^^ {case west => Surroundings(Anything, Anything, west, Anything)})
      | (s ^^ {case south => Surroundings(Anything, Anything, Anything, south)}) 
    )
    
  def n: Parser[RelativeDescription] = 
    (   ("n" | "N")~("orth" | "")~":"~>wall
      | failure("expected *, x, or + after tag N(orth):") )

  def e: Parser[RelativeDescription] = 
    (   ("e" | "E")~("ast" | "")~":"~>wall
      | failure("expected *, x, or + after tag E(ast):") )

  def w: Parser[RelativeDescription] = 
    (   ("w" | "W")~("est" | "")~":"~>wall
      | failure("expected *, x, or + after tag W(est):") )

  def s: Parser[RelativeDescription] = 
    (   ("s" | "S")~("outh" | "")~":"~>wall
      | failure("expected *, x, or + after tag S(outh):") )

  def wall: Parser[RelativeDescription] = 
    (   ("*" ^^^ Anything)
      | ("x" ^^^ Open)
      | ("+" ^^^ Blocked) )

  def go: Parser[MoveDirection] = ("g" | "G")~"o:"~>direction

  def direction: Parser[MoveDirection] =
    (   (("x"|"X") ^^^ StayHere)
      | (("n" | "N")~("orth" | "") ^^^ North)
      | (("e" | "E")~("ast" | "") ^^^ East)
      | (("w" | "W")~("est" | "") ^^^ West)
      | (("s" | "S")~("outh" | "") ^^^ South) )

  def state: Parser[State] =
    (("s"|"S")~"tate:"~>"""\d\d?""".r) ^^ State

  def newState: Parser[State] =
    ("n"|"N")~"ew"~>state
}
