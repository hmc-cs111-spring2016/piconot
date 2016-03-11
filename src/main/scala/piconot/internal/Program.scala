package piconot.internal
import picolib.maze.Maze
import picolib.semantics._
import Directionlist._
import SurroundingsChecker._
import RuleWrapper._
import RuleListWrapper._
import java.io.File
import scalafx.application.JFXApp

object Program extends JFXApp {
//  val z:List[Rule] = go (left) until ((blocked on left and down) and (open on up))
//  println(z)
//  val z1:RuleListWrapper = (go (left) until (blocked on left)) then 
//                           (go (up) until (blocked on down)) thenLoop
//                           (go (down) until (blocked on up)) then
//                           (go (left) until (blocked on left)) then
//                           (go (back))
//  println(loopState)
//  println(toRuleList(z1))
  
  val emptyRoomProgram = (go (left) until (blocked on left)) then
(go (up) until (blocked on up and left)) thenLoop
(go (down) until (blocked on down)) then
(go (right)) then
(go (up) until (blocked on up)) then
(go (right)) then
(go (back))

  val emptyMaze = Maze("resources" + File.separator + "empty.txt")

  object EmptyBot extends Picobot(emptyMaze, emptyRoomProgram)
    with TextDisplay with GUIDisplay

  stage = EmptyBot.mainStage

  EmptyBot.run()
  
}
