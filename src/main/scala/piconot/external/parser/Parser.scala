package piconot.external.parser

import scala.util.parsing.combinator._
import piconot.external.ir._
import picolib.semantics._

object PiconotParser extends JavaTokenParsers with PackratParsers {

    // parsing interface
    def apply(s: String): ParseResult[OurProgram] = parseAll(prgm, s)
    
    lazy val prgm: PackratParser[OurProgram] = 
      (   emptyProgram ^^ {case e => EmptyProgram()}
        | state~prgm ^^ {case s~p => CurrProgram(s, p)}
      )
      
    lazy val state: PackratParser[OurState] =
      (   "def"~num~":"~rules ^^ {case "def"~i~":"~rs => CurrState(i, rs)}
        | emptyState ^^ {case e => EmptyState()}
      )
    
    lazy val rules: PackratParser[OurRules] = 
      (   rule~rules ^^ {case r~rs => RuleList(r,rs)}
        | emptyRule ^^ {case e => EmptyRules()}
      )
    
    lazy val rule: PackratParser[OurRule] = 
      (    "while"~surr~action ^^ {case "while"~s~a => WhileRule(s, a)}
         | "if"~surr~action~num ^^ {case "if"~s~a~st => IfRule(s, a, st)}
      )

    lazy val action: PackratParser[OurAction] =
      (    "move"~"N" ^^ {case e => MoveN()}
         | "move"~"E" ^^ {case e => MoveE()}
         | "move"~"W" ^^ {case e => MoveW()}
         | "move"~"S" ^^ {case e => MoveS()}
         | "move"~"X" ^^ {case e => MoveX()}
      )
      
    lazy val surr: PackratParser[OurSurroundings] =
      (   "N"~surr ^^ {case "N"~s => SurrN(s)}
        | "E"~surr ^^ {case "E"~s => SurrE(s)}
        | "W"~surr ^^ {case "W"~s => SurrW(s)}
        | "S"~surr ^^ {case "S"~s => SurrS(s)}
        | "~N"~surr ^^ {case "~N"~s => SurrNotN(s)}
        | "~E"~surr ^^ {case "~E"~s => SurrNotE(s)}
        | "~W"~surr ^^ {case "~W"~s => SurrNotW(s)}
        | "~S"~surr ^^ {case "~S"~s => SurrNotS(s)}
        | emptySurr ^^ {case s => SurrEmpty()}
          )

    def num: Parser[Int] = wholeNumber ^^ {s ⇒ s.toInt}
    def emptyProgram: Parser[OurProgram] = " " ^^ {s => EmptyProgram()}
    def emptyState: Parser[OurState] = " " ^^ {s => EmptyState()}
    def emptyRule: Parser[OurRules] = " " ^^ {s => EmptyRules()}
    def emptySurr: Parser[OurSurroundings] = " " ^^ {s => SurrEmpty()}
}
