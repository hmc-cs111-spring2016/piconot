package piconot.internal

import scala.language.implicitConversions
import picolib.maze.Maze
import picolib.semantics._
import scala.collection.mutable.MutableList
import scalafx.application.JFXApp

/**
 * @author ben
 * 
 * This design and implementation leaves a *lot* to be desired. But I wanted to 
 * give a sample solution that wasn't too innovative on syntax design and that
 * contained a couple of implementation techniques that you might not have 
 * thought of at first.
 * 
 * No, the name "Picobor" is not a typo -- this is a boring language :).
 */

class Picobor(val mazeFilename: String) extends JFXApp {
  
  // the list of rules, which is built up as the Picobor program executes
  private val rules = MutableList.empty[Rule]  
  
  def addRule(rule: Rule) = rules += rule
  
  def run() = {
    val maze = Maze(mazeFilename)
    val picobot = 
      new Picobot(maze, rules.toList) with TextDisplay with GUIDisplay
    stage = picobot.mainStage
    picobot.run()
  }

  /////////////////////////////////////////////////////////////////////////////
  // Internal DSL definition
  /////////////////////////////////////////////////////////////////////////////

  // a class to gather the start state and environment description
  // (yep, this is the pain we go through for our users)
  implicit class PatternMatcher(n: Int) {
    def `xxxx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Open, Open))
    def `xxx*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Open, Anything))
    def `xxxS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Open, Blocked))
    def `xx*x` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Anything, Open))
    def `xx**` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Anything, Anything))
    def `xx*S` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Anything, Blocked))
    def `xxWx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Blocked, Open))
    def `xxW*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Blocked, Anything))
    def `xxWS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Open, Blocked, Blocked))
    def `x*xx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Open, Open))
    def `x*x*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Open, Anything))
    def `x*xS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Open, Blocked))
    def `x**x` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Anything, Open))
    def `x***` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Anything, Anything))
    def `x**S` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Anything, Blocked))
    def `x*Wx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Blocked, Open))
    def `x*W*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Blocked, Anything))
    def `x*WS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Anything, Blocked, Blocked))
    def `xExx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Open, Open))
    def `xEx*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Open, Anything))
    def `xExS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Open, Blocked))
    def `xE*x` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Anything, Open))
    def `xE**` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Anything, Anything))
    def `xE*S` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Anything, Blocked))
    def `xEWx` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Blocked, Open))
    def `xEW*` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Blocked, Anything))
    def `xEWS` = new RuleBuilder(new State(n.toString), Surroundings(Open, Blocked, Blocked, Blocked))
    def `*xxx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Open, Open))
    def `*xx*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Open, Anything))
    def `*xxS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Open, Blocked))
    def `*x*x` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Anything, Open))
    def `*x**` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Anything, Anything))
    def `*x*S` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Anything, Blocked))
    def `*xWx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Blocked, Open))
    def `*xW*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Blocked, Anything))
    def `*xWS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Open, Blocked, Blocked))
    def `**xx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Open, Open))
    def `**x*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Open, Anything))
    def `**xS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Open, Blocked))
    def `***x` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Anything, Open))
    def `****` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Anything, Anything))
    def `***S` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Anything, Blocked))
    def `**Wx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Blocked, Open))
    def `**W*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Blocked, Anything))
    def `**WS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Anything, Blocked, Blocked))
    def `*Exx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Open, Open))
    def `*Ex*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Open, Anything))
    def `*ExS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Open, Blocked))
    def `*E*x` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Anything, Open))
    def `*E**` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Anything, Anything))
    def `*E*S` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Anything, Blocked))
    def `*EWx` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Blocked, Open))
    def `*EW*` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Blocked, Anything))
    def `*EWS` = new RuleBuilder(new State(n.toString), Surroundings(Anything, Blocked, Blocked, Blocked))
    def `Nxxx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Open, Open))
    def `Nxx*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Open, Anything))
    def `NxxS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Open, Blocked))
    def `Nx*x` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Anything, Open))
    def `Nx**` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Anything, Anything))
    def `Nx*S` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Anything, Blocked))
    def `NxWx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Blocked, Open))
    def `NxW*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Blocked, Anything))
    def `NxWS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Open, Blocked, Blocked))
    def `N*xx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Open, Open))
    def `N*x*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Open, Anything))
    def `N*xS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Open, Blocked))
    def `N**x` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Anything, Open))
    def `N***` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Anything, Anything))
    def `N**S` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Anything, Blocked))
    def `N*Wx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Blocked, Open))
    def `N*W*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Blocked, Anything))
    def `N*WS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Anything, Blocked, Blocked))
    def `NExx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Open, Open))
    def `NEx*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Open, Anything))
    def `NExS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Open, Blocked))
    def `NE*x` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Anything, Open))
    def `NE**` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Anything, Anything))
    def `NE*S` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Anything, Blocked))
    def `NEWx` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Blocked, Open))
    def `NEW*` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Blocked, Anything))
    def `NEWS` = new RuleBuilder(new State(n.toString), Surroundings(Blocked, Blocked, Blocked, Blocked))
  }
  
  // a class to gather the move direction and next state
  class RHSBuilder(val moveDirection: MoveDirection) {
    def apply(nextState: Int): (MoveDirection, State) =
      (moveDirection, new State(nextState.toString))
  }
  
  // internal DSL names for move directions
  object N extends RHSBuilder(North)
  object E extends RHSBuilder(East)
  object W extends RHSBuilder(West)
  object S extends RHSBuilder(South)
  object X extends RHSBuilder(StayHere)
  
  // a class to build a rule from its parts and add the rule to the running
  // list of rules in this picobor program
  class RuleBuilder(val startState: State, val surroundings: Surroundings) {
    val program = Picobor.this
    def →(rhs: (MoveDirection, State)) = {
      val (moveDirection, nextState) = rhs
      val rule = new Rule(startState, surroundings, moveDirection, nextState)
      program.addRule(rule)
    }
  }
}
