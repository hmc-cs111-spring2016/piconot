package piconot.external

//import scala.tools.nsc.EvalLoop
import piconot.SurroundingsPrime
import java.io.File
import scalafx.application.JFXApp
import picolib.maze.Maze
import picolib.semantics._

object Application extends JFXApp {
    val source = scala.io.Source.fromFile("external-Maze.txt")
    val lines = try source.mkString finally source.close()
    val emptyMaze = Maze("resources" + File.separator + "maze.txt")
    
    PicobotParser(lines) match {
        case PicobotParser.Success(t, _) ⇒ {
            val rules = eval(t)
            object EmptyBot extends Picobot(emptyMaze, rules) with TextDisplay with GUIDisplay
            stage = EmptyBot.mainStage
            EmptyBot.run()
        }
        case e: PicobotParser.NoSuccess  ⇒ println(e)
    }
}