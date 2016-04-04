package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._
import piconot.internal.OurRule._
import piconot.internal.OurSurroundings._
import piconot.internal.OurAction._


object Empty extends JFXApp {
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  val ourProgram = List(
  (OurState (0)
    (loop (notN (*)) (move(North)))
    (loop (notW (N (*))) (move (West)))
    (cond (N (W (notE (notS (*)))))
        (move (South))
        (1))).rules,

  (OurState (1)
    (loop (notS (*)) (move(South)))
    (cond (S (*))
        (move(East))
        (2))).rules,

  (OurState (2)
    (loop (notN (*)) (move(North)))
    (cond (N (*)) 
        (move(East))
        (1))).rules)
        
   object OurEmptyBot extends Picobot(emptyMaze, ourProgram.flatten)
    with TextDisplay with GUIDisplay
    
  stage = OurEmptyBot.mainStage

  OurEmptyBot.run()
}