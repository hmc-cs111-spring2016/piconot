package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

import scala.language.implicitConversions
import scala.collection.mutable.ListBuffer


object PicoDSL {
  var numStates = 0
  var stateToInt = Map[String,Int]()
  var rules = new ListBuffer[Rule]()
  var currentState:String = ""
  
  implicit def toAction(state: String):Action = {
    ConcatAct(stop, state)
  }
  
  
  def WhenIn(state: String) = {
    if (!(stateToInt contains state)) {
      stateToInt = stateToInt + (state -> numStates)
      numStates = numStates + 1
    }
    currentState = state
    
    ((body: Unit) => (body))
  }
  
  def If(cond: Condition = defaultCondition)(act: Action):Unit = {
    val state = act.nextState.getOrElse(currentState);
    if (!(stateToInt contains state)) {
      stateToInt = stateToInt + (state -> numStates)
      numStates = numStates + 1
     }
    val nextState = State(stateToInt(state).toString)
    
    PicoDSL.rules += Rule(State(PicoDSL.stateToInt(currentState).toString), cond.surroundings, act.movement, nextState)
  }
  
  def runOnMaze(str: String):Unit = Now.runOnMaze(str)
}

object Now extends JFXApp {
  def runOnMaze(str: String):Unit = {
    val emptyMaze = Maze(str)
    val rules = PicoDSL.rules.toList
    
    object EmptyBot extends Picobot(emptyMaze, rules) with TextDisplay with GUIDisplay
    stage = EmptyBot.mainStage

    EmptyBot.run()
  }
}


abstract class Condition {
  def and(cond: Condition):Condition = ConcatCond(this, cond)
  def but(cond: Condition):Condition = ConcatCond(this, cond)
  def surroundings: Surroundings
}

object defaultCondition extends Condition {
  def surroundings = Surroundings(Anything, Anything, Anything, Anything)
}
object canMoveLeft extends Condition {
  def surroundings = Surroundings(Anything, Anything, Open, Anything)
}
object cannotMoveLeft extends Condition {
  def surroundings = Surroundings(Anything, Anything, Blocked, Anything)
}
object canMoveRight extends Condition {
  def surroundings = Surroundings(Anything, Open, Anything, Anything)
}
object cannotMoveRight extends Condition {
  def surroundings = Surroundings(Anything, Blocked, Anything, Anything)
}
object canMoveUp extends Condition {
  def surroundings = Surroundings(Open, Anything, Anything, Anything)
}
object cannotMoveUp extends Condition {
  def surroundings = Surroundings(Blocked, Anything, Anything, Anything)
}
object canMoveDown extends Condition {
  def surroundings = Surroundings(Anything, Anything, Anything, Open)
}
object cannotMoveDown extends Condition {
  def surroundings = Surroundings(Anything, Anything, Anything, Blocked)
}
case class ConcatCond(val c1: Condition, val c2: Condition) extends Condition {
  def surroundings = {
    val Surroundings(c1Up, c1Right, c1Left, c1Down) = c1.surroundings
    val Surroundings(c2Up, c2Right, c2Left, c2Down) = c2.surroundings
    
    val up = if (c1Up == Anything) c2Up else c1Up
    val right = if (c1Right == Anything) c2Right else c1Right
    val down = if (c1Down == Anything) c2Down else c1Down
    val left = if (c1Left == Anything) c2Left else c1Left
    
    Surroundings(up, right, left, down)
  }
}

abstract class Action {
  def and(str: String):Action = ConcatAct(this, str)
  def movement = if (1==1) StayHere else North
  def nextState: Option[String] = None
}

object moveLeft extends Action {
  override def movement = West
}
object moveRight extends Action {
  override def movement = East
}
object moveUp extends Action {
  override def movement = North
}
object moveDown extends Action {
  override def movement = South
}
case class ConcatAct(val act: Action, val state: String) extends Action {
  override def movement = act.movement
  override def nextState = Some(state)
}
object stop extends Action