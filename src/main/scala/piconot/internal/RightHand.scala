import piconot.internal._
import piconot.internal.PicoDSL._
import scala.language.implicitConversions
import scalafx.application.JFXApp


// A picobot program that can solve a maze using the right-hand rule

object fillMaze extends JFXApp {

  WhenIn ("moveUp") {
    If (canMoveRight) (moveRight and "moveRight")        // we can turn right
    If (cannotMoveRight but canMoveUp) (moveUp)          // we can't turn right, so try going up
    If (cannotMoveUp and cannotMoveRight) ("moveDown")   // we can't turn right or go up, so try turning around
  }

  WhenIn ("moveRight") {
    If (canMoveDown) (moveDown and "moveDown")             // we can turn right
    If (cannotMoveDown but canMoveRight) (moveRight)       // we can't turn right, so try going forward
    If (cannotMoveRight and cannotMoveDown) ("moveLeft")   // we can't turn right or go down, so try turning around
  }

  WhenIn ("moveLeft") {
    If (canMoveUp) (moveUp and "moveUp")                 // we can turn right
    If (cannotMoveUp but canMoveLeft) (moveLeft)         // we can't turn right, so try going forward (left)
    If (cannotMoveUp and cannotMoveLeft) ("moveRight")   // we can't turn right or go forward, so try turning around
  }

  WhenIn ("moveDown") {
    If (canMoveLeft) (moveLeft and "moveLeft")          // we can turn right
    If (cannotMoveLeft but canMoveDown) (moveDown)      // we can't turn right, so try going forward
    If (cannotMoveLeft and cannotMoveDown) ("moveUp")   // we can't turn right or go forward, so try turning around
  }

  runOnMaze("resources/maze.txt")
}