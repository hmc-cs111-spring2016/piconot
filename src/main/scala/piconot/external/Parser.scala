package piconot.external

import scala.util.parsing.combinator._

object PicobotParser extends JavaTokenParsers with PackratParsers {
    def apply(s: String): ParseResult[Program] = parseAll(program, s)
    
    def program: Parser[Program] = {
        (  "start" ~ name ~ repsep(task, "") ^^ {case "start" ~ n ~ ts ⇒ Program(n, ts)}
         | "start" ~ name ~ repsep(rule, "") ^^ {case "start" ~ n ~ rs ⇒ Program(n, rs)} )
    }
    
    def task: Parser[Task] = {
        "task" ~ name ~ ":" ~ "start" ~ name ~ repsep(rule, "") ^^ {case "task" ~ n ~ ":" ~ "start" ~ s ~ rs ⇒ Task(n, s, rs)}
    }
    
    def rule: Parser[Rule] = {
        name ~ ":" ~ repsep(subRule, "") ^^ {case n ~ ":" ~ srs ⇒ Rule(n, srs)}
    }
    
    def subRule: Parser[SubRule] = {
        condition ~ action ^^ {case c ~ a ⇒ SubRule(c, a)}
    }
    
    def condition: Parser[Condition] = {
        (  "if" ~ wallCheck ~ "and" ~ emptyCheck ^^ {case "if" ~ w ~ "and" ~ e ⇒ Condition(w, e)}
		 | "if" ~ emptyCheck ~ "and" ~ wallCheck ^^ {case "if" ~ e ~ "and" ~ w ⇒ Condition(w, e)} 
		 | "if" ~ wallCheck ^^ {case "if" ~ w ⇒ Condition(walls = w)}
		 | "if" ~ emptyCheck ^^ {case "if" ~ e ⇒ Condition(empties = e)} )
    }
    
    def emptyCheck: Parser[isEmpty] = {
        "isEmpty" ~ "(" ~ repsep(direction, ",") ~ ")" ^^ {case "isEmpty" ~ "(" ~ ds ~ ")" ⇒ isEmpty(ds)}
    }
    
    def wallCheck: Parser[isWall] = {
        "isWall" ~ "(" ~ repsep(direction, ",") ~ ")" ^^ {case "isWall" ~ "(" ~ ds ~ ")" ⇒ isWall(ds)}
    }
    
    def action: Parser[Action] = {
        (  move ~ next ^^ {case m ~ n ⇒ Action(m,n)}
         | move ^^ {m ⇒ Action(move = m)}
         | next ^^ {n ⇒ Action(next = n)} )
    }
    
    def move: Parser[Move] = {
        "move" ~ "(" ~ direction ~ ")" ^^ {case "move" ~ "(" ~ d ~ ")" ⇒ Move(d)}
    }
    
    def next: Parser[Next] = {
        "next" ~ "(" ~ name ~ ")" ^^ {case "next" ~ "(" ~ n ~ ")" ⇒ Next(n)}
    }
    
    def direction: Parser[Direction] = {
        (  "Up" ^^ {d ⇒ Up}
         | "Down" ^^ {d ⇒ Down}
         | "Left" ^^ {d ⇒ Left}
         | "Right" ^^ {d ⇒ Right} )
    }
    
    def name: Parser[Name] = {
        "[a-zA-Z]+".r ^^ {s ⇒ Name(s)}
    }
}