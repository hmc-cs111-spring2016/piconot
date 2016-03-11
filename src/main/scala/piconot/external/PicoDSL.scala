package piconot.external

import piconot.external.PicoDSL.interpreter.semantics
import piconot.external.PicoDSL.parser.PicoParser
import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp
import scala.io.Source


object Piconot {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {println("Not enough args"); return}
    
    val mapPath:String = args(0)
    val filePath:String = args(1)
    
    val code: String = Source.fromFile(filePath).mkString
    val parse = PicoParser.apply(code)
    if (!parse.successful) {println(parse); return}
    val rules: List[Rule] = semantics.eval(parse.get)
    
    val emptyMaze = Maze(mapPath)
    
    object helper extends JFXApp {
      object EmptyBot extends Picobot(emptyMaze, rules) with TextDisplay with GUIDisplay
      stage = EmptyBot.mainStage
      EmptyBot.run()
    }
    
    helper.main(Array())
  }
}