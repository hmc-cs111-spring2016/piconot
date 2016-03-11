package piconot

import picolib.maze.Maze
import picolib.semantics._

package object external {
	def eval(program: Program): List[picolib.semantics.Rule] = {

		val newProgram = replaceTask(program)
		val ruleList = program.execs.map({
			case x: Task => taskToRules(x)
			case x: Rule => ruleToRules(x)

			})
	}

	// replacing all Task Names with their start task, outer level rules with @.rule
	def replaceTask(program: Program): Program = {
		val taskNameList = program.execs.map({
			case x: Task => (x.name, x.name +'.'+ x.start)
			case x: Rule => (x.name, "@."+x.name)
			})
		
	}


	def tasktoRules(task: Task): List[picolib.semantics.Rule] = {

	}
	def ruletoRules(rule: Rule): List[picolib.semantics.Rule] = {

	}

}

case class Program(val start: Name, val execs: List[Execution])

class Execution()

case class Task(name: Name, start: Name, rules: List[Rule]) extends Execution

case class Rule(name: Name, subRules: List[SubRule]) extends Execution
case class SubRule(condition: Condition, action: Action)

case class Condition(walls: isWall = isWall(List()), empties: isEmpty = isEmpty(List()))
case class isWall(direction: List[Direction])
case class isEmpty(direction: List[Direction])

case class Action(move: Move = Move(None), next: Next = null)
case class Move(direction: Direction)
case class Next(name: Name)


case class Direction()

object Up extends Direction {
    override def toString() = "Up"
}
object Down extends Direction{
    override def toString() = "Down"
}
object Left extends Direction{
    override def toString() = "Left"
}
object Right extends Direction{
    override def toString() = "Right"
}
object None extends Direction{
    override def toString() = "None"
}

case class Name(name: String)

//taskName.ruleName //check if next(name) is task first then if it matches a ruleName? How decide which rule name to match with?