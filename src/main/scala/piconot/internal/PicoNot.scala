package piconot.internal

import scala.language.implicitConversions
import picolib.maze.Maze
import picolib.semantics._
import scala.collection.mutable.MutableList
import scalafx.application.JFXApp

class Piconot(val mazeFilename: String) extends JFXApp {

  private val rules = MutableList.empty[Rule]

  def addRule(rule: Rule) = rules += rule

  def run() = {
    val maze = Maze(mazeFilename)
    val picobot =
      new Picobot(maze, rules.toList) with TextDisplay with GUIDisplay
    stage = picobot.mainStage
    picobot.run()
  }

  implicit class LHSBuilder(s: State) {
    def apply(north: RelativeDescription = *, east: RelativeDescription = *,
      west: RelativeDescription = *, south: RelativeDescription = *) = {
      new LHS(s, Surroundings(north, east, west, south))
    }
  }

  val * = Anything
  val w = Blocked
  val x = Open
  val N:MoveDirection = North
  val E:MoveDirection = East
  val W:MoveDirection = West
  val S:MoveDirection = South


  class LHS(val state: State, surroundings: Surroundings) {
    val program = Piconot.this

    def apply(go: MoveDirection = StayHere, newState: State = state) = {
      val rule = new Rule(state, surroundings, go, newState)
      program.addRule(rule)
    }
  }

  implicit def intToState(n: Int):State = State(n)

  object State {
    def apply(state: Int) = new State(state.toString)
  }
}
