package piconot.external

import picolib.semantics._
import piconot.external.parser.PiconotParser
import piconot.external.semantics.semantics.eval

import scalafx.application.JFXApp

import picolib.maze.Maze

object Piconot extends App {
  //val emptyMaze = Maze("resources" + File.separator + "empty.txt")
  
  val emptyMaze: Maze = Maze(args(0))
  val botFile: String = args(1)
  var rules: List[Rule] = PiconotParser(botFile) match {
    case PiconotParser.Success(t, _) => eval(t)
    case e: PiconotParser.NoSuccess => List[Rule]()
  }
  
  object EmptyBot extends Picobot(emptyMaze, rules)
    with TextDisplay// with GUIDisplay

  //stage = EmptyBot.mainStage

  EmptyBot.run()
}
