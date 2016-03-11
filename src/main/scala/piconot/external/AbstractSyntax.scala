package piconot.external;

case class Program(val start: Name, val execs: List[Execution])

class Execution()

case class Task(name: Name, start: Name, rules: List[Rule]) extends Execution

case class Rule(name: Name, subRules: List[SubRule]) extends Execution
case class SubRule(condition: Condition, action: Action)

case class Condition(walls: isWall = isWall(List()), empties: isEmpty = isEmpty(List()))
case class isWall(directions: List[Direction])
case class isEmpty(directions: List[Direction])

case class Action(move: Move = Move(None), next: Next = Next(Name("")))
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

case class Name(value: String)
