package piconot

import picolib.maze.Maze
import picolib.semantics._

package object internal {
   
   //converts our directions to the built in RelativeDirections
    def Up = North
    def Down = South
    def Left = West
    def Right = East
    def None = StayHere

    //Takes up to 4 arguments for directions in which there is no wall
    // returns a SurroundingsPrime with all the empty directions marked.
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
    // returns a SurroundingsPrime with all the blocked directions marked.
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


     //Takes in a direction and a statusand returns a SurroundingsPrime with 
     //that direction's status filled in.
    private def checkDirection(status: RelativeDescription, dir: MoveDirection): SurroundingsPrime = {
        dir match {
            case North => SurroundingsPrime(north = status)
            case East  => SurroundingsPrime(east = status)
            case West  => SurroundingsPrime(west = status)
            case South => SurroundingsPrime(south = status)
            case _     => SurroundingsPrime()
        }
    }
}

 //Equivalent to a Surroundings object that we can modify to use the and method
case class SurroundingsPrime(val north: RelativeDescription = Anything, 
                 val east: RelativeDescription = Anything,
                 val west: RelativeDescription = Anything,
                 val south: RelativeDescription = Anything ) {

    // takes two SurroundingsPrime objects and combines them
    def and(that: SurroundingsPrime): SurroundingsPrime = {
        val newNorth = combine(this.north, that.north)
        val newEast = combine(this.east, that.east)
        val newWest = combine(this.west, that.west)
        val newSouth = combine(this.south, that.south)

        SurroundingsPrime(newNorth, newEast, newWest, newSouth)

    }

    // Helper function for and() that takes two Relative Descriptions and combines them
    // returns an error if more than one of them is not equal to Anything
    def combine(thisStatus: RelativeDescription, thatStatus: RelativeDescription): RelativeDescription  = {
        if (thisStatus == Anything && thatStatus == Anything) {
            Anything
        } else if (thisStatus == thatStatus) {
            throw new Exception("You put the same direction in twice.")
        } else if (thisStatus == Anything ) {
            thatStatus
        } else  if (thatStatus == Anything) {
            thisStatus
        } else {
            throw new Exception("You put the same direction in both isEmpty() and isWall().")
        }

    }

}
