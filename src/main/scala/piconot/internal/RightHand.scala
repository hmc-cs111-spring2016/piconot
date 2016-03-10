package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._
import piconot.internal.OurRule._
import piconot.internal.OurSurroundings._
import piconot.internal.OurAction._


object RightHand extends JFXApp {
  val rightHandMaze = Maze("resources" + File.separator + "maze.txt")

  var ourProgram = OurProgram 
  (OurState ( 0 )
    (cond (notE (*)) (move(East)) (1))
    (loop (notN (E (*))) (move(North)))
    (cond (N (E (*))) (move (StayHere)) (3)))

  (OurState ( 1 )
    (cond (notS (*)) (move(South)) (3))
    (loop (notE (S (*))) (move(East)))
    (cond (S (E (*))) (move (StayHere)) (2)))
    

  (OurState ( 2 )
    (cond (notN (*)) (move(North)) (0))
    (loop (notW (N (*))) (move(West)))
    (cond (N (W (*))) (move (StayHere)) (1)))
    

  (OurState ( 3 )
    (cond (notW (*)) (move(West)) (2))
    (loop (notS (W (*))) (move(South)))
    (cond (W (S (*))) (move (StayHere)) (0)))
   
  object RightHandBot extends Picobot(rightHandMaze, ourProgram.rules)
    with TextDisplay with GUIDisplay

  stage = RightHandBot.mainStage

  RightHandBot.run()
}