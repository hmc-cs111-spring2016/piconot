package piconot.internal

import picolib.semantics._
import scala.collection.mutable.MutableList

//import scala.language.implicitConversions
import picolib.maze.Maze
import scalafx.application.JFXApp

class API(val mazeFilename: String) extends JFXApp {
  
  /**
   * List of rules created by using the API
   */
  val rules = MutableList.empty[RuleStructure]
  
  // string of possible unicode arrow characters "↑→↓←"
  
  /**
   * Case class for containing the block and open strings
   */
  case class BlockOpen(block: String, open: String)
  
  /**
   * Case class for containing the move and toState strings
   */
  case class MoveToState(move: String, toState: String)
  
  /**
   * Class for constructing and converting rules
   */
  class RuleStructure(stateName: String) {    
    /**
     * List of PRules case classes
     */
    val prules = MutableList.empty[PartialRule]
    
    /**
     * Last BlockOpen used. This variable is needed to link the contents of the + and -> operators 
     */
    var lastBlockOpen: BlockOpen = null
    
    /**
     * Operator for adding new prule
     */
    def +(block: String, open: String) = {
      lastBlockOpen = BlockOpen(block,open)
      this
    }
    
    /**
     * Operator for adding the rest of the new prule
     */
    def ->(move: String, toState: String) = { // ooo +/-
      if(lastBlockOpen == null) throw new Exception("Need + before -> operator") // error check
      val lastMoveToState = MoveToState(move, toState)
      val prule = mkPRule(lastBlockOpen, lastMoveToState)
      prules += prule
      lastBlockOpen = null
      this
    }
    
    /**
     * Makes a prule from the BlockOpen and MoveToState case classes
     */
    private def mkPRule(bo: BlockOpen, ms: MoveToState): PartialRule = {
      /**
       * Creates list of contains results used to map to Open, Blocked,
       * Anything, or detect duplicate arrow error
       */
      val bk = ("↑→←↓" map { x =>  (bo.block indexOf x) != -1 }) toList;
      val op = ("↑→←↓" map { x =>  (bo.open  indexOf x) != -1 }) toList;
      
      /**
       * maps arrows to Open, Blocked, Anything, or an Exception and makes a
       * Surroundings case class
       */
      val surr = (bk zip op) map { case (x,y) => (x,y) match {
          case (false,true)  => Open
          case (true,false)  => Blocked
          case (false,false) => Anything
          case (true,true)   => throw new Exception("ERROR: Duplicate arrows") // ERROR
        }
      }
      val surroundings = Surroundings(surr(0), surr(1), surr(2), surr(3))
      
      /**
       * Maps arrow to direction or stay here 
       */
      val dir = (ms move) match {
        case "↑" => North
        case "→" => East
        case "←" => West
        case "↓" => South
        case "." => StayHere
      }
      
      /**
       * Creates state case class
       */
      val toState = State(ms toState)
      
      /**
       * Creates case class for the PartialRule
       */
      PartialRule(surroundings, dir, toState)
    }
    
    def translate(): List[Rule] = prules.map { x => Rule(State(stateName), x.surr, x.move, x.toState) } toList

  }
  
  /**
   * Case class for a PartialRule (i.e. a rule without a state to belong to)
   */
  case class PartialRule(surr: Surroundings, move: MoveDirection, toState: State)
  
  /**
   * Function wrapper around RuleStructure for syntatic sugar
   */
  def state(sn: String):RuleStructure = {
    val rule = new RuleStructure(sn) // lower case b/c conflict
    rules += rule
    rule
  }
  
  /**
   * Called to run the internal DSL
   */
  def run() = {
    val maze = Maze(mazeFilename)
    val flatrules = (rules.toList map { x => x.translate }) flatten;
    println(flatrules)
    val picobot = new Picobot(maze, flatrules) with TextDisplay with GUIDisplay
    stage = picobot.mainStage
    picobot.run()
  }

}