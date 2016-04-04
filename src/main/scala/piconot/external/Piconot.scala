package piconot.external

import scala.io.Source
import picolib.semantics._
import piconot.external.parser.PiconotParser
import piconot.external.semantics.semantics.eval

import scalafx.application.JFXApp

import picolib.maze.Maze

object Piconot extends JFXApp {
  val args = parameters.raw
  val maze: Maze = Maze(args(0))  
  val botFile = Source.fromFile(args(1)).getLines.mkString
    
  var rules: List[Rule] = PiconotParser(botFile) match {
    case PiconotParser.Success(t, _) => eval(t)
    case e: PiconotParser.NoSuccess => {
      println("BRO DO YOU EVEN PARSE")
      List[Rule]()
    }
  }
        
  object EmptyBot extends Picobot(maze, rules) 
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage

  EmptyBot.run()
}
