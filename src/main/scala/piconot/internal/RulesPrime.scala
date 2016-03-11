package piconot.internal

import picolib.semantics._
import piconot.SurroundingsPrime
import scala.collection.mutable.MutableList


object Rules {
    var rules: MutableList[RulePrime] = MutableList()
    
    def addRule (rule: RulePrime) = {
        rules += rule
    }
}

class RulePrime (var start: State = null, var surroundings: Surroundings = null, 
                 var moveDir: MoveDirection = null, var end: State = null) {
                
    def next(newEnd: String) = {
        this.end = State(newEnd)
    }
    
    def move(dir: MoveDirection) = new RulePrime(moveDir = dir)

    
}

class RulePrimeList {
    val rules: List[RulePrime] = List()
    
    def If (surr: SurroundingsPrime, partialRule: RulePrime) =  {
        partialRule.surroundings = Surroundings(surr.north, surr.east, surr.west, surr.south)
        val newRules = rules :: List(partialRule)
    }
}
