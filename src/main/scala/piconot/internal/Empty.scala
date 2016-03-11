import piconot.internal._
import piconot.internal.PicoDSL._
import scala.language.implicitConversions
import scalafx.application.JFXApp


// A Picobot program that can fill an empty room

object fillEmptyRoom extends JFXApp {

  // move to the top left

  WhenIn ("moveLeftState") {
    If (canMoveLeft) (moveLeft)            // go all the way to the left
    If (cannotMoveLeft) ("moveUpState")   // can't go left anymore, so try to go up
  }

  WhenIn ("moveUpState") {
    If (canMoveUp) (moveUp)                                             // go all the way to the top
    If (cannotMoveUp and canMoveDown) (moveDown and "fillColumnDown")   // can't go up any more, so try to go down
  }


  // fill from top to bottom, left to right

  WhenIn ("fillColumnDown") {   // fill this column to the bottom
    If (canMoveDown) (moveDown)                                           // go all the way to the bottom
    If (cannotMoveDown but canMoveRight) (moveRight and "fillColumnUp")   // can't go down anymore, so try to go right
  }

  WhenIn ("fillColumnUp") {     // fill this column to the top
    If (canMoveUp) (moveUp)                                               // go all the way to the top
    If (cannotMoveUp but canMoveRight) (moveRight and "fillColumnDown")   // can't go up anymore, so try to go right
  }

  runOnMaze("resources/empty.txt")
}