package piconot.internal

import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp
import scala.language.implicitConversions
import scala.collection.mutable.ListBuffer

object PicoDSL {
  var numStates = 0                                          // used to keep track of how many states have been used
  val nameToState = collection.mutable.Map[String,State]()   // used to convert state names to picolib states
  var rules = new ListBuffer[Rule]()                         // used to store all the rules encountered thus far
  var currentStateName:String = ""                           // a hack needed so that If statements know which state they are in
  
  // makes sure we have allocated a state number for the given state name
  def addStateName(stateName: String) = {
    if (!(nameToState contains stateName)) {
      nameToState(stateName) = State(numStates.toString)
      numStates = numStates + 1
    }
  }
  
  // when only a string is provided in the action field, convert it to an action
  implicit def toAction(state: String):Action = {
    ConcatAct(stop, state)
  }
  
  // updates the current state name and processes the rules for that state
  def WhenIn(stateName: String) = {
    addStateName(stateName)
    currentStateName = stateName
    
    ((body: Unit) => (body)) // a hack that causes the rules for the state to be processed
  }
  
  // adds a new rule
  def If(cond: Condition = defaultCondition)(action: Action):Unit = {
    val stateName = action.nextState.getOrElse(currentStateName);
    addStateName(stateName)
    val nextState = nameToState(stateName)
    
    rules += Rule(nameToState(currentStateName), cond.surroundings, action.movement, nextState)
  }
  
  // runs rules encountered so far on given map
  def runOnMaze(str: String):Unit = runOnMazeHelper.runOnMaze(str)
  
  // helper object that extends JFXApp so we don't have to
  object runOnMazeHelper extends JFXApp {
    def runOnMaze(str: String):Unit = {
      val emptyMaze = Maze(str)
      val rules = PicoDSL.rules.toList
      
      object EmptyBot extends Picobot(emptyMaze, rules) with TextDisplay with GUIDisplay
      stage = EmptyBot.mainStage

      EmptyBot.run()
    }
  }
  
  // merges the constraints of two Surroundings
  def mergeSurroundings(c1: Surroundings, c2: Surroundings):Surroundings = {
    val Surroundings(c1Up, c1Right, c1Left, c1Down) = c1
    val Surroundings(c2Up, c2Right, c2Left, c2Down) = c2
    
    val up = if (c1Up == Anything) c2Up else c1Up
    val right = if (c1Right == Anything) c2Right else c1Right
    val down = if (c1Down == Anything) c2Down else c1Down
    val left = if (c1Left == Anything) c2Left else c1Left
    
    Surroundings(up, right, left, down)
  }
}

// represents the conditions under which a rule can be used
class Condition(val surroundings: Surroundings) {
  def and(cond: Condition):Condition = {new Condition(PicoDSL.mergeSurroundings(surroundings, cond.surroundings))}
  def but(cond: Condition):Condition = and(cond)
}
object canMoveLeft extends Condition(Surroundings(Anything, Anything, Open, Anything))
object cannotMoveLeft extends Condition(Surroundings(Anything, Anything, Blocked, Anything))
object canMoveRight extends Condition(Surroundings(Anything, Open, Anything, Anything))
object cannotMoveRight extends Condition(Surroundings(Anything, Blocked, Anything, Anything))
object canMoveUp extends Condition(Surroundings(Open, Anything, Anything, Anything))
object cannotMoveUp extends Condition(Surroundings(Blocked, Anything, Anything, Anything))
object canMoveDown extends Condition(Surroundings(Anything, Anything, Anything, Open))
object cannotMoveDown extends Condition(Surroundings(Anything, Anything, Anything, Blocked))
object defaultCondition extends Condition(Surroundings(Anything, Anything, Anything, Anything))

// represents the actions taken if a rule is followed
abstract class Action(val movement:MoveDirection, val nextState: Option[String] = None) {
  def and(str: String):Action = ConcatAct(this, str)
}
object moveLeft extends Action(West)
object moveRight extends Action(East)
object moveUp extends Action(North)
object moveDown extends Action(South)
object stop extends Action(StayHere)
case class ConcatAct(val act: Action, val state: String) extends Action(act.movement, Some(state))