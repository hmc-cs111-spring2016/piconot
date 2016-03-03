package piconot

import picolib.maze.Maze
import picolib.semantics._

package object internal {
   
   //converts our directions to the built in RelativeDirections
    def Up = North
    def Down = South
    def Left = West
    def Right = East

 //Takes up to 4 arguments for directions in which there is no wall
     // returns a SouroundingsPrime with all the empty directions marked.
     def isEmpty(dir1: MoveDirection = null,
                 dir2: MoveDirection = null,
                 dir3: MoveDirection = null,
                 dir4: MoveDirection = null ) = {

        val first = checkDirection(Open, dir1)
        val second = checkDirection(Open, dir2)
        val third = checkDirection(Open, dir3)
        val fourth = checkDirection(Open, dir4)

        first and second and third and fourth
     }
     
     //Takes up to 4 arguments for directions in which there is a wall
     // returns a SouroundingsPrime with all the blocked directions marked.
     def isWall(dir1: MoveDirection = null, 
                dir2: MoveDirection = null,
                dir3: MoveDirection = null,
                dir4: MoveDirection = null ) = {

        val first = checkDirection(Blocked, dir1)
        val second = checkDirection(Blocked, dir2)
        val third = checkDirection(Blocked, dir3)
        val fourth = checkDirection(Blocked, dir4)
        
        first and second and third and fourth
         
     }


     //Takes in a direction and a statusand returns a SouroundingsPrime with 
     //that direction's status filled in.
     private def checkDirection(val status: RelativeDescription, val dir: MoveDirection): SouroundingsPrime{
        dir match {
            case North => SouroundingsPrime(status, Anything, Anything, Anything)
            case East => SouroundingsPrime(Anything, status, Anything, Anything)
            case West => SouroundingsPrime(Anything, Anything, status, Anything)
            case South => SouroundingsPrime(Anything, Anything, Anything, status)
        }
     }

 }

 //Equivalent to a Souroundings object that we can modify to use the and method
case class SouroundingsPrime(val up: RelativeDescription = null, 
                 val right: RelativeDescription = null,
                 val left: RelativeDescription = null,
                 val down: RelativeDescription = null ) = {

    // takes two SouroundingsPrime objects and combines them
    def and(that: SouroundingsPrime): SouroundingsPrime = {
        val newUp = combine(this.up, that.up)
        val newRight = combine(this.right, that.right)
        val newLeft = combine(this.left, that.left)
        val newDown = combine(this.down, that.down)

        SouroundingsPrime(newUp, newRight, newLeft, newDown)

    }

    // Helper function for and() that takes two Relative Descriptions and combines them
    // returns an error if more than one of them is not equal to Anything
    def combine(val thisStatus: RelativeDescription, val thatStatus: RelativeDescription): RelativeDirection  = {
        if (thisStatus == Anything && thatStatus == Anything) {
            Anything
        } else if (thisStatus == thatStatus) {
            throw new IllegalArgumentExcpetion("You put the same direction in twice.")
        } else if (thisStatus == Anything ) {
            thatStatus
        } else  if (thatStatus == Anything{
            thisStatus
        } else {
            throw new IllegalArgumentExcpetion("You put the same direction in both isEmpty() and isWall().")
        }

    }

}
