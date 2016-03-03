package piconot.internal

import picolib.maze.Maze
import picolib.semantics._

class rule (val name: String, subRules: Unit) {
     subRules
     
     def isEmpty(dir1: MoveDirection = null,
                 dir2: MoveDirection = null,
                 dir3: MoveDirection = null,
                 dir4: MoveDirection = null ) = {
         
     
     }
     def and () = Surroundings()
     
     def isWall(dir1: MoveDirection = null, 
                dir2: MoveDirection = null,
                dir3: MoveDirection = null,
                dir4: MoveDirection = null ) = {
         
     }
}

class Conditional {
    
}

object isEmpty extends Conditional{
    
}

object isWall extends Conditional{
    def and (other: Conditional) {
        
    }
}