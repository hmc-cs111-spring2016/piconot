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
  (   ("("~state~surrounding~";"~go~","~newState~")") ^^ 
      { case "("~stateNow~walls~";"~move~","~stateAfter~")" =>
          Rule(stateNow, walls, move, stateAfter) }

    | ("("~state~surrounding~";"~go~")") ^^
      { case "("~stateNow~walls~";"~move~")" =>
          Rule(stateNow, walls, move, stateNow) }

    | ("("~state~surrounding~";"~newState~")") ^^
      { case "("~stateNow~walls~";"~stateAfter~")" =>
          Rule(stateNow, walls, defaultMove, stateAfter) }

    | "("~state~surrounding~")"~>failure("reached end of rule with no move or state change. Picobot will get stuck if it follows this rule.")
  )

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
  // by more wall information. It could've just been part of surrounding,
  // but the code is more readable this way.
  def nfirst: Parser[Surroundings] = n~","~noN ^^
  { case first~","~rest => rest(first) }

  // Like nfirst, but for rules where east is given first
  def efirst: Parser[Surroundings] = e~","~noE ^^
  { case first~","~rest => rest(first) }

  // like nfirst, but for rules where west is given first
  def wfirst: Parser[Surroundings] = w~","~noW ^^
  { case first~","~rest => rest(first) }

  // like nfirst, but for rules where south is given first
  def sfirst: Parser[Surroundings] = s~","~noS ^^
  { case first~","~rest => rest(first) }

  // Parses the rest (in the Racket sense) of wall info for rules 
  // where north is first and followed by info for other directions
  def noN: Parser[RelativeDescription => Surroundings] = 
  (   nefirst 
    | nwfirst 
    | nsfirst 
    | (e ^^ {case e => Surroundings(_:RelativeDescription, e, Anything, Anything)})
    | (w ^^ {case w => Surroundings(_:RelativeDescription, Anything, w, Anything)})
    | (s ^^ {case s => Surroundings(_:RelativeDescription, Anything, Anything, s)})
  )

  // This is for rules where north is given first, followed by east, and
  // then wall info for at least one more direction. It's more of a bridge
  // between noN and noNE than anything else. It gets wall info for east,
  // and then lets noNE deal with west and/or south. This could easily have just
  // been part of noN, but it looks nicer this way.
  def nefirst: Parser[RelativeDescription => Surroundings] = e~","~noNE ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like nefirst, but north is first followed by west. Bridges noN and
  // noNW.
  def nwfirst: Parser[RelativeDescription => Surroundings] = w~","~noNW ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like nefirst, but north is first followed by south. Bridges noN and
  // noNS.
  def nsfirst: Parser[RelativeDescription => Surroundings] = s~","~noNS ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like noN, but for rules where east is first.
  def noE: Parser[RelativeDescription => Surroundings] = 
  (   enfirst 
    | ewfirst 
    | esfirst
    | (n ^^ {case n => Surroundings(n, _:RelativeDescription, Anything, Anything)})
    | (w ^^ {case w => Surroundings(Anything, _:RelativeDescription, w, Anything)})
    | (s ^^ {case s => Surroundings(Anything, _:RelativeDescription, Anything, s)})
  )

  // Like nefirst, but for rules where east is first followed by north.
  // Bridges noE and noNE.
  def enfirst: Parser[RelativeDescription => Surroundings] = n~","~noNE ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Like nefirst, but for rules where east is first followed by west.
  // Bridges noE and noEW.
  def ewfirst: Parser[RelativeDescription => Surroundings] = w~","~noEW ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like nefirst, but for rules where east is first followed by south.
  // Bridges noE and noES.
  def esfirst: Parser[RelativeDescription => Surroundings] = s~","~noES ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like noN, but for rules where west is first.
  def noW: Parser[RelativeDescription => Surroundings] = 
  (   wnfirst 
    | wefirst 
    | wsfirst 
    | (n ^^ {case n => Surroundings(n, Anything, _:RelativeDescription, Anything)})
    | (e ^^ {case e => Surroundings(Anything, e, _:RelativeDescription, Anything)})
    | (s ^^ {case s => Surroundings(Anything, Anything, _:RelativeDescription, s)})
  )

  // Like nefirst, but for rules where west is first followed by north.
  // Bridges noW and noNW
  def wnfirst: Parser[RelativeDescription => Surroundings] = n~","~noNW ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Like nefirst, but for rules where west is first followed by east.
  // Bridges noW and noEW.
  def wefirst: Parser[RelativeDescription => Surroundings] = e~","~noEW ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Like nefirst, but for rules where west is first followed by south.
  // Bridges noW and noWS.
  def wsfirst: Parser[RelativeDescription => Surroundings] = s~","~noWS ^^
  { case first~","~rest => rest(_:RelativeDescription, first) }

  // Like noN, but for rules where south is first.
  def noS: Parser[RelativeDescription => Surroundings] = 
  (   snfirst 
    | sefirst 
    | swfirst 
    | (n ^^ {case n => Surroundings(n, Anything, Anything, _:RelativeDescription)})
    | (e ^^ {case e => Surroundings(Anything, e, Anything, _:RelativeDescription)})
    | (w ^^ {case w => Surroundings(Anything, Anything, w, _:RelativeDescription)})
  )

  // Like nefirst, but for rules where south is first followed by north.
  // Bridges noS and noNS.
  def snfirst: Parser[RelativeDescription => Surroundings] = n~","~noNS ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Like nefirst, but for rules where south is first followed by east.
  // Bridges noS and noES.
  def sefirst: Parser[RelativeDescription => Surroundings] = e~","~noES ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Like nefirst, but for rules where south is first followed by west.
  // Bridges noS and noWS.
  def swfirst: Parser[RelativeDescription => Surroundings] = w~","~noWS ^^
  { case first~","~rest => rest(first, _:RelativeDescription) }

  // Sort of like noN in that it parses the rest of the wall info in a rule. 
  // The difference here is that it parses everything after the first TWO pieces 
  // of wall info, where those first two are for north and east in either order.
  // That is, this finishes parsing wall info for rules where wall info is given
  // in any of the orders NEW, NES, NEWS, NESW, ENW, ENS, ENWS, and ENSW.
  def noNE: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   wslast
    | swlast 
    | (w ^^ 
      {case w => Surroundings(_:RelativeDescription, _:RelativeDescription, w, Anything)})
    | (s ^^
      {case s => Surroundings(_:RelativeDescription, _:RelativeDescription, Anything, s)})
  )

  // This does the actual work of noNE for rules where both west and south are
  // given, in that order.
  def wslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~s ^^ 
  { case w~","~s => Surroundings(_:RelativeDescription, _:RelativeDescription, w, s) }

  // Like wslast, but for rules where south is given third and west fourth.
  def swlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~w ^^ 
  { case s~","~w => Surroundings(_:RelativeDescription, _:RelativeDescription, w, s) }

  // Like noNE, but for rules where north and west are the first two given.
  def noNW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   eslast
    | selast 
    | (e ^^ 
      {case e => Surroundings(_:RelativeDescription, e, _:RelativeDescription, Anything)})
    | (s ^^
      {case s => Surroundings(_:RelativeDescription, Anything, _:RelativeDescription, s)})
  )

  // This does the actual work of noNW for rules where both east and south are
  // given, in that order.
  def eslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~s ^^ 
  { case e~","~s => Surroundings(_:RelativeDescription, e, _:RelativeDescription, s) }

  // Like eslast, but for rules where south is given third and east fourth.
  def selast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~e ^^ 
  { case s~","~e => Surroundings(_:RelativeDescription, e, _:RelativeDescription, s) }

  // Like noNE, but for rules where north and south are the first two given.
  def noNS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   ewlast
    | welast
    | (e ^^ 
      {case e => Surroundings(_:RelativeDescription, e, Anything, _:RelativeDescription)})
    | (w ^^
      {case w => Surroundings(_:RelativeDescription, Anything, w, _:RelativeDescription)})
    )

  // This does the actual work of noNS for rules where both east and west are
  // given, in that order.
  def ewlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~w ^^ 
  { case e~","~w => Surroundings(_:RelativeDescription, e, w, _:RelativeDescription) }

  // Like ewlast, but for rules where west is given third and east fourth.
  def welast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~e ^^ 
  { case w~","~e => Surroundings(_:RelativeDescription, e, w, _:RelativeDescription) }

  // Like noNE, but for rules where north and south are the first two given.
  def noEW: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nslast
    | snlast
    | (n ^^ 
      {case n => Surroundings(n, _:RelativeDescription, _:RelativeDescription, Anything)})
    | (s ^^
      {case s => Surroundings(Anything, _:RelativeDescription, _:RelativeDescription, s)})
  )

  // This does the actual work of noEW for rules where both north and south are
  // given, in that order.
  def nslast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~s ^^ 
  { case n~","~s => Surroundings(n, _:RelativeDescription, _:RelativeDescription, s) }

  // Like nslast, but for rules where south is given third and north fourth.
  def snlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  s~","~n ^^ 
  { case s~","~n => Surroundings(n, _:RelativeDescription, _:RelativeDescription, s) }

  // Like noNE, but for rules where east and south are the first two given.
  def noES: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nwlast
    | wnlast
    | (n ^^ 
      {case n => Surroundings(n, _:RelativeDescription, Anything, _:RelativeDescription)})
    | (w ^^
      {case w => Surroundings(Anything, _:RelativeDescription, w, _:RelativeDescription)})
  )

  // This does the actual work of noES for rules where both north and west are
  // given, in that order.
  def nwlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~w ^^ 
  { case n~","~w => Surroundings(n, _:RelativeDescription, w, _:RelativeDescription) }

  // Like nwlast, but for rules where west is given third and north fourth.
  def wnlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  w~","~n ^^ 
  { case w~","~n => Surroundings(n, _:RelativeDescription, w, _:RelativeDescription) }

  // Like noNE, but for rules where west and south are the first two given.
  def noWS: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  (   nelast
    | enlast
    | (n ^^ 
      {case n => Surroundings(n, Anything, _:RelativeDescription, _:RelativeDescription)})
    | (e ^^
      {case e => Surroundings(Anything, e, _:RelativeDescription, _:RelativeDescription)})
  )

  // This does the actual work of noWS for rules where both north and east are
  // given, in that order.
  def nelast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  n~","~e ^^ 
  { case n~","~e => Surroundings(n, e, _:RelativeDescription, _:RelativeDescription) }

  // Like nelast, but for rules where east is given third and north fourth.
  def enlast: Parser[(RelativeDescription, RelativeDescription) => Surroundings] =
  e~","~n ^^ 
  { case e~","~n => Surroundings(n, e, _:RelativeDescription, _:RelativeDescription) }


  // This is for rules that give wall info for a single direction.
  def onewall: Parser[Surroundings] = 
    (   (n ^^ {case north => Surroundings(north, Anything, Anything, Anything)})
      | (e ^^ {case east => Surroundings(Anything, east, Anything, Anything)})
      | (w ^^ {case west => Surroundings(Anything, Anything, west, Anything)})
      | (s ^^ {case south => Surroundings(Anything, Anything, Anything, south)}) 
    )
    
  // Parses the tag for north wall info, then delegates.
  def n: Parser[RelativeDescription] = ("n" | "N")~("orth" | "")~>nwall

  // This and ewall, wwall, and swall allow different error messages
  // when an unexpected token is found after a different wall info tag.
  // This lets the error tell the user why it's happening THERE and not
  // somewhere else.
  def nwall: Parser[RelativeDescription] =
    (   wall
      | failure("expected *, x, or + after tag N(orth)") )

  // Parses the tag for east wall info, then delegates.
  def e: Parser[RelativeDescription] = ("e" | "E")~("ast" | "")~>ewall

  // see nwall
  def ewall: Parser[RelativeDescription] =
    (   wall
      | failure("expected *, x, or + after tag E(ast)") )

  // Parses the tag for west wall info, then delegates.
  def w: Parser[RelativeDescription] = ("w" | "W")~("est" | "")~>wwall

  // see nwall
  def wwall: Parser[RelativeDescription] =
    (   wall
      | failure("expected *, x, or + after tag W(est)") )

  // Parses the tag for south wall info, then delegates.
  def s: Parser[RelativeDescription] = ("s" | "S")~("outh" | "")~>swall

  // see nwall
  def swall: Parser[RelativeDescription] =
    (   wall
      | failure("expected *, x, or + after tag S(outh)") )

  // Parses the token after a wall info tag. * for wildcard,
  // x for no wall, + for wall.
  def wall: Parser[RelativeDescription] = 
    (   ("*" ^^^ Anything)
      | ("x" ^^^ Open)
      | ("+" ^^^ Blocked) )

  // Parses the tag for movement direction, then delegates.
  def go: Parser[MoveDirection] = ("g" | "G")~"o"~>direction

  // Parses the token after the movement direction tag. Mostly the same as in 
  // the original picobot, but directions can be given as full words or first 
  // letters, and the first letter is not case-sensitive.
  def direction: Parser[MoveDirection] =
    (   (("x"|"X") ^^^ StayHere)
      | (("n" | "N")~("orth" | "") ^^^ North)
      | (("e" | "E")~("ast" | "") ^^^ East)
      | (("w" | "W")~("est" | "") ^^^ West)
      | (("s" | "S")~("outh" | "") ^^^ South) )

  // Parses both the state tag and state number.
  def state: Parser[State] =
    (("s"|"S")~"tate"~>"""\d\d?""".r) ^^ State

  // Sticks "new" at the front of state.
  def newState: Parser[State] =
    ("n"|"N")~"ew"~>state
}
