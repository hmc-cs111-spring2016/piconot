package piconot.external

import piconot.external.PicoDSL.interpreter.semantics
import piconot.external.PicoDSL.parser.PicoParser
import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp
import scala.io.Source


object Piconot {
  def main(args: Array[String]): Unit = {
    // needs a path to a map and a code file
    if (args.length < 2) {println("Not enough args"); return}
    
    // extract args
    val mapPath:String = args(0)
    val filePath:String = args(1)
    
    // read file into string, parse, and interpret
    val code: String = Source.fromFile(filePath).mkString
    val parse = PicoParser.apply(code)
    if (!parse.successful) {println(parse); return}    // display parsing error (if any)
    val rules: List[Rule] = semantics.eval(parse.get)
    
    // run rules using picolib
    val emptyMaze = Maze(mapPath)
    object helper extends JFXApp {
      object EmptyBot extends Picobot(emptyMaze, rules) with TextDisplay with GUIDisplay
      stage = EmptyBot.mainStage
      EmptyBot.run()
    }
    helper.main(Array())
  }
}