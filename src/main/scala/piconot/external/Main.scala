package piconot.external

import java.io.FileNotFoundException
import picolib.maze.Maze
import picolib.semantics._
import scalafx.application.JFXApp
import piconot.external.parser.PicoParser

// most of this code is adapted from the solution
object Piconot extends JFXApp {

  val args = parameters.raw
  
  // Error handling: did the user pass two arguments?
  if (args.length != 2) {
    println(usage)
    sys.exit(1)
  }

  // parse the maze file
  val mazeFileName = args(0)
  val maze = Maze(getFileLines(mazeFileName))
  
  // parse the program file
  val programFilename = args(1)  
  val program = PicoParser(getFileContents(programFilename))

  // process the results of parsing
  program match {      
    // Error handling: syntax errors
    case e: PicoParser.NoSuccess  ⇒ println(e)
    
    // if parsing succeeded...
    case PicoParser.Success(t, _) ⇒ {
      val picobotAST = Translate.convertAST(program.get) // converts our AST into the Picolib notation 
      val bot = new Picobot(maze, picobotAST) with TextDisplay with GUIDisplay
      bot.run
    }
  }

  /** A string that describes how to use the program **/
  def usage = "usage: piconot.external.Piconot <maze-file> <rules-file>"

  /**
   * Given a filename, get a list of the lines in the file
   */
  def getFileLines(filename: String): List[String] =
    try {
      io.Source.fromFile(filename).getLines().toList
    }
    catch { // Error handling: non-existent file
      case e: FileNotFoundException ⇒ { println(e.getMessage()); sys.exit(1) }
    }

  /**
   * Given a filename, get the contents of the file
   */
  def getFileContents(filename: String): String =
    try {
      io.Source.fromFile(filename).mkString
    }
    catch { // Error handling: non-existent file
      case e: FileNotFoundException ⇒ { println(e.getMessage()); sys.exit(1) }
    }
}