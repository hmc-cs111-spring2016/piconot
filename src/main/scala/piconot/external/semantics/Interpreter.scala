package piconot.external
import picolib.maze.Maze
import picolib.semantics._


import ir._
package object semantics {
  var currentState:Int = 0
  var loopState:State = State("-1")
  
  def eval(expr: Expr): List[Rule] = {
    postProcess(evalRuleList(expr))
  }
  
  def postProcess(rules: List[Rule]): List[Rule] = {
    val lastRule = rules.last
    val newRule = Rule(
            rules.last.startState,
            Surroundings(Anything, Anything, Anything, Anything),
            StayHere,
            loopState
          )
    rules.dropRight(1) :+ newRule
  }
  
  def evalRuleList(expr: Expr): List[Rule] = {
    currentState += 2
    expr match {
      case Go(Back()) => List(
          Rule(
            State(currentState.toString()),
            Surroundings(Anything, Anything, Anything, Anything),
            StayHere,
            State("if you see this something has gone horribly wrong")
          )
        )
      case Go(direction) => List(
          Rule(
            State(currentState.toString()),
            Surroundings(Anything, Anything, Anything, Anything),
            evalDirection(direction),
            State((currentState + 1).toString())
          )
        )
      case GoUntil(direction, directionlist) => {
          
          List(
            Rule(
              State(currentState.toString()),
              createSurroundings(evalDirectionList(directionlist)),
              StayHere,
              State((currentState + 1).toString())
            ),
            Rule(
              State(currentState.toString()),
              Surroundings(Anything, Anything, Anything, Anything),
              evalDirection(direction),
              State(currentState.toString())
            )
          )
        }
        
      case Then(ruleGroup, otherGroup) => {
      	val firstGroup = evalRuleList(ruleGroup)
      	val rest = evalRuleList(otherGroup)
    	  val newTransitionRule:Rule = Rule(
            firstGroup.head.startState,
            firstGroup.head.surroundings,
            firstGroup.head.moveDirection,
            rest.head.startState
          )
        (newTransitionRule +: firstGroup.tail) ++ rest
      }
      case ThenLoop(ruleGroup, otherGroup) => {
      	val firstGroup = evalRuleList(ruleGroup)
      	val rest = evalRuleList(otherGroup)
      	loopState = rest.head.startState
    	  val newTransitionRule:Rule = Rule(
            firstGroup.head.startState,
            firstGroup.head.surroundings,
            firstGroup.head.moveDirection,
            rest.head.startState
          )
        (newTransitionRule +: firstGroup.tail) ++ rest
      }
      case _ => throw new Exception("illegal call to evalRuleList")
    }  
  }
  
  def createSurroundings(dList: List[MoveDirection]):Surroundings = {
    val newSurroundings = List(North, East, West, South).map(x => {
      if (dList.contains(x))
        Blocked
      else
        Anything
    })
    new Surroundings(newSurroundings(0), newSurroundings(1), newSurroundings(2), newSurroundings(3))
  }
  
  def evalDirectionList(expr: Expr): List[MoveDirection] = expr match {
    case ListOfDirections(Cons(d, EmptyDirectionList())) => List(evalDirection(d))
    case ListOfDirections(Cons(d, dList)) => evalDirection(d) +: evalDirectionList(dList)
    case _ => throw new Exception("illegal call to evalDirectionList")
  }
  
  def evalDirection(expr: Expr): MoveDirection = expr match {
    case SingleDirection(expr) => evalDirection(expr)
    case Up() => North
    case Left() => West
    case Right() => East
    case Down() => South
    case _ => throw new Exception("illegal call to evalDirection")

  }
  
}