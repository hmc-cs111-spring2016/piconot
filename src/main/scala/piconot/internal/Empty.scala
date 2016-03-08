package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._
import piconot.internal.OurRule._
import piconot.internal.OurSurroundings._
import piconot.internal.OurAction._


class Empty extends JFXApp {
  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  var ourProgram = OurProgram 
  (OurState ( 0 )
    (loop (notN (*)) (move(North)))
    (loop (notN (W (*))) (move (West)))
    (cond (N (W (notE (notS (*)))))
        (move (South))
        (1) ))

  (OurState (1)
    (loop (notS (*)) (move(South)))
    (cond (S (*))
        (move(East))
        (2)))

  (OurState (2)
    (loop (notN (*)) (move(North)))
    (cond (N (*))
        (move(East))
        (1)))
        
   object EmptyBot extends Picobot(emptyMaze, ourProgram.rules)
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage

  EmptyBot.run()
}