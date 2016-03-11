package  piconot.external.PicoDSL.parser

import scala.util.parsing.combinator._
import piconot.external.PicoDSL.ir._
import picolib.semantics._


object PicoParser extends JavaTokenParsers with PackratParsers {

    // parsing interface
    def apply(s: String): ParseResult[ProgramAST] = parseAll(program, removeComments(s))
    
    // removes # and everything after for each line
    def removeComments(s: String) = s.split("\n").map((s: String) => s.split("#")(0)).mkString("\n")
    
    lazy val program: PackratParser[ProgramAST] = (
        stateBlock~program ^^ {case s~p => ProgramAST(s :: p.states)}
      | stateBlock ^^ {sb => ProgramAST(List(sb))}
    )
    
    // state block
    lazy val stateBlock: PackratParser[StateBlockAST] = (
      ident~":"~rules ^^ {case i~":"~r => StateBlockAST(i, r.map((r: RuleAST) => if (r.stateName == "") {RuleAST(r.conditions, r.direction, i)} else {r}))}
    )
    
    lazy val rules: PackratParser[List[RuleAST]] = (
        If~rules ^^ {case i~r => i :: r}
      | If ^^ {r => List(r)}
    )
    
    lazy val If: PackratParser[RuleAST] = (
      "If "~conds~","~action ^^ {case "If "~c~","~a => RuleAST(c, a.direction, a.stateName)}
    )
    
    lazy val conds: PackratParser[List[ConditionAST]] = (
        "can move "~dirs~("and"|"but")~conds ^^ {case "can move "~d~("and"|"but")~c => d.map((md: MoveDirection) => ConditionAST(true, md)) ++ c}
      | "cannot move "~dirs~("and"|"but")~conds ^^ {case "cannot move "~d~("and"|"but")~c => d.map((md: MoveDirection) => ConditionAST(false, md)) ++ c}
      | "can move "~dirs ^^ {case "can move "~d => d.map((md: MoveDirection) => ConditionAST(true, md))}
      | "cannot move "~dirs ^^ {case "cannot move "~d => d.map((md: MoveDirection) => ConditionAST(false, md))}
    )
    
    lazy val dirs: PackratParser[List[MoveDirection]] = (
        dir~"or"~dirs ^^ {case d~"or"~dd => d :: dd}
      | dir ^^ {case d => List(d)}
    )
    
    lazy val dir: PackratParser[MoveDirection] = (
        "up" ^^ {_ => North}
      | "down" ^^ {_ => South}
      | "right" ^^ {_ => East}
      | "left" ^^ {_ => West}
    )
    
    lazy val action: PackratParser[RuleAST] = (
        "move "~dir~"and"~ident ^^ {case "move "~d~"and"~i => RuleAST(List(), d, i)}
      | "move "~dir ^^ {case "move "~d => RuleAST(List(), d, "")}
      | ident ^^ {i => RuleAST(List(), StayHere, i)}
    )
 }
