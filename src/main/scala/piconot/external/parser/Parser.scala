package piconot.external.parser

import scala.util.parsing.combinator._
import piconot.external.ir._
object PicoParser extends JavaTokenParsers with PackratParsers {
  
  def apply(s: String): ParseResult[Expr] = parseAll(ruleList, s)
  
  lazy val ruleList: PackratParser[Expr] = 
    (   ruleGroup~"then"~ruleList ^^ {case l~"then"~r => Then(l, r)}
      | ruleGroup~"then"~ruleGroup ^^ {case l~"then"~r => Then(l, r)}
      | ruleGroup~"thenLoop"~ruleList ^^ {case l~"thenLoop"~r => ThenLoop(l, r)}
      | ruleGroup~"thenLoop"~ruleGroup ^^ {case l~"thenLoop"~r => ThenLoop(l, r)})
      
  lazy val ruleGroup: PackratParser[Expr] = 
    (   "go"~direction~"until"~surroundings ^^ {case "go"~d~"until"~s => GoUntil(d, s)}
      | "go"~direction ^^ {case "go"~d => Go(d)})
  
      
  lazy val surroundings: PackratParser[Expr] = 
    (   "blocked on"~directionList ^^ {case "blocked on"~d => ListOfDirections(d)} )
  
    
  lazy val directionList: PackratParser[Expr] = 
    (   direction~"and"~directionList ^^ {case d~"and"~dl => Cons(SingleDirection(d), ListOfDirections(dl))}
      | direction ^^ {case d => Cons(SingleDirection(d), EmptyDirectionList())})
  
      
  lazy val direction: PackratParser[Expr] = 
    (   "left" ^^ {case "left" => Left()}
      | "right" ^^ {case "right" => Right()}
      | "up" ^^ {case "up" => Up()}
      | "down" ^^ {case "down" => Down()}
      | "back" ^^ {case "back" => Back()})
  
  
}