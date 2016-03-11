package piconot.external
import piconot.external.parser.PicoParser
import piconot.external.semantics._
import picolib.maze.Maze
import picolib.semantics._
import java.io.File
import scalafx.application.JFXApp


object Program extends JFXApp {
  //override def main(args: Array[String]) ={
    val args = parameters.raw
    val mazeFile = args(0)
    val instructions = scala.io.Source.fromFile(args(1)).mkString
    println(instructions)
    val program = PicoParser(instructions) match {
      case PicoParser.Success(t, _) ⇒ eval(t)
      case PicoParser.NoSuccess(e) => throw new Exception("something has gone wrong parsing!")
    }
    
   object Bot extends Picobot(Maze(mazeFile), program)
    with TextDisplay with GUIDisplay
  stage = Bot.mainStage
  Bot.run()
  //}
}