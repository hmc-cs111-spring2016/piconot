package piconot.internal

import java.io.File
import scalafx.application.JFXApp


import picolib.maze.Maze
import picolib.semantics._

/**
 *  This is an intentionally bad internal language, but it uses all the parts of
 *  the picolib library that you might need to implement your language
 */

object EmptyRoom extends JFXApp {
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  val rules = List(
    
    /////////////////////////////////////////////////////////
    // State 0: go West
    /////////////////////////////////////////////////////////
    
    // as long as West is unblocked, go West
    Rule( 
      State("zero"), 
      Surroundings(Anything, Anything, Open, Anything), 
      Left, 
      State("zero")
    ),

    // can't go West anymore, so try to go North (by transitioning to State 1)
    Rule( 
      State("zero"), 
      Surroundings(Anything, Anything, Blocked, Anything), 
      StayHere, 
      State("one")
    ),

    /////////////////////////////////////////////////////////
    // State 1: go North
    /////////////////////////////////////////////////////////

    // as long as North is unblocked, go North
    Rule( 
      State("one"), 
      Surroundings(Open, Anything, Anything, Anything), 
      North, 
      State("one")
    ),

    // can't go North any more, so try to go South (by transitioning to State 2)
    Rule( 
      State("one"), 
      Surroundings(Blocked, Anything, Anything, Open), 
      South, 
      State("two")
    ), 

    /////////////////////////////////////////////////////////
    // States 2 & 3: fill from North to South, West to East
    /////////////////////////////////////////////////////////

    // State 2: fill this column from North to South
    Rule( 
      State("two"), 
      Surroundings(Anything, Anything, Anything, Open), 
      South, 
      State("two")
    ), 

    // can't go South anymore, move one column to the East and go North
    // (by transitioning to State 3)
    Rule( 
      State("two"), 
      Surroundings(Anything, Open, Anything, Blocked), 
      East, 
      State("three")
    ),

    // State 3: fill this column from South to North
    Rule( 
      State("three"), 
      Surroundings(Open, Anything, Anything, Anything), 
      North, 
      State("three")
    ),

    // can't go North anymore, move one column to the East and go South
    // (by transitioning to State 2)
    Rule( 
      State("three"), 
      Surroundings(Blocked, Open, Anything, Anything), 
      East, 
      State("two")
    )
  )

  object EmptyBot extends Picobot(emptyMaze, rules)
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage

  EmptyBot.run()

}
