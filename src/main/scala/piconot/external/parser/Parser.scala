package  piconot.external.PicoDSL.parser

import scala.util.parsing.combinator._
import piconot.external.PicoDSL.ir._
import picolib.semantics._


object PicoParser extends JavaTokenParsers with PackratParsers {

    // parsing interface
    def apply(s: String): ParseResult[ProgramAST] = parseAll(program, removeComments(s))
    
    // removes # and everything after it on each line
    def removeComments(s: String) = s.split("\n").map((s: String) => s.split("#")(0)).mkString("\n")
    
    // program is broken down into one or more state blocks
    lazy val program: PackratParser[ProgramAST] = (
        stateBlock~program ^^ {case s~p => ProgramAST(s :: p.states)}
      | stateBlock ^^ {sb => ProgramAST(List(sb))}
    )
    
    // state blocks consist of a state name (followed by a colon) and rules
    lazy val stateBlock: PackratParser[StateBlockAST] = (
      ident~":"~rules ^^ {case i~":"~r => StateBlockAST(i, r.map((r: RuleAST) => if (r.stateName == "") {RuleAST(r.conditions, r.direction, i)} else {r}))}
    )
    
    // rules consist of one or more If statements
    lazy val rules: PackratParser[List[RuleAST]] = (
        If~rules ^^ {case i~r => i :: r}
      | If ^^ {r => List(r)}
    )
    
    // If statements contain "If" followed by conditions, a comma, and actions (where to move / transition to)
    lazy val If: PackratParser[RuleAST] = (
      "If "~conds~","~action ^^ {case "If "~c~","~a => RuleAST(c, a.direction, a.stateName)}
    )
    
    // conditions consist of a bunch of "can / cannot move"s in certain directions concatenated by "and"s or "but"s
    lazy val conds: PackratParser[List[ConditionAST]] = (
        "can move "~dirs~("and"|"but")~conds ^^ {case "can move "~d~("and"|"but")~c => d.map((md: MoveDirection) => ConditionAST(true, md)) ++ c}
      | "cannot move "~dirs~("and"|"but")~conds ^^ {case "cannot move "~d~("and"|"but")~c => d.map((md: MoveDirection) => ConditionAST(false, md)) ++ c}
      | "can move "~dirs ^^ {case "can move "~d => d.map((md: MoveDirection) => ConditionAST(true, md))}
      | "cannot move "~dirs ^^ {case "cannot move "~d => d.map((md: MoveDirection) => ConditionAST(false, md))}
    )
    
    // each set of directions consists of one or more directions separated by "or"s
    lazy val dirs: PackratParser[List[MoveDirection]] = (
        dir~"or"~dirs ^^ {case d~"or"~dd => d :: dd}
      | dir ^^ {case d => List(d)}
    )
    
    // convert direction to MoveDirection
    lazy val dir: PackratParser[MoveDirection] = (
        "up" ^^ {_ => North}
      | "down" ^^ {_ => South}
      | "right" ^^ {_ => East}
      | "left" ^^ {_ => West}
    )
    
    // an action explicitly specifies a movement direction, a state transition, or both with an and in between
    lazy val action: PackratParser[RuleAST] = (
        "move "~dir~"and"~ident ^^ {case "move "~d~"and"~i => RuleAST(List(), d, i)}
      | "move "~dir ^^ {case "move "~d => RuleAST(List(), d, "")}
      | ident ^^ {i => RuleAST(List(), StayHere, i)}
    )
 }
