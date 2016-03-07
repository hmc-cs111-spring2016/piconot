package piconot.internal

import picolib.maze.Maze
import picolib.semantics._

object Evaluator {
        
//  val north = N
//  require(eval(north) == Surroundings(taken, anything, anything, anything) )
//  
//  
  def eval(surr: OurSurroundings): Surroundings = surr match {
      case noSurroundings => Surroundings(Anything, Anything, Anything, Anything)
      case North(restSurr) => {
        var result = eval(restSurr)
        Surroundings(Blocked, result.east, result.west, result.south)
      }
      case East(restSurr) => {
        var result = eval(restSurr)
        Surroundings(result.north, Blocked, result.west, result.south)
      }
      case West(restSurr) => {
        var result = eval(restSurr)
        Surroundings(result.north, result.east, Blocked, result.south)
      }
      case South(restSurr) => {
        var result = eval(restSurr)
        Surroundings(result.north, result.east, result.west, Blocked)
      }
      case Not(North(restSurr)) => {
        var result = eval(restSurr)
        Surroundings(Open, result.east, result.west, result.south)
      }
      case Not(East(restSurr)) => {
        var result = eval(restSurr)
        Surroundings(result.north, Open, result.west, result.south)
      }
      case Not(West(restSurr)) => {
        var result = eval(restSurr)
        Surroundings(result.north, result.east, Open, result.south)
      }
      case Not(South(restSurr)) => {
        var result = eval(restSurr)
        Surroundings(result.north, result.east, result.west, Open)
      }
  }
  
  def evalMove(moveDir: OurMoveDirection): MoveDirection = moveDir match {
    case Move(dir: Char) => {
      MoveDirection(dir)
    }
  }
    
  def evalRule(rule: OurRule): Rule = rule match {
    case If(surr: OurSurroundings, moveDir: OurMoveDirection, nextState: OurState) => {
      Rule(***, eval(surr), evalMove(moveDir), evalState(nextState))
    }
    case While(surr: OurSurroundings, moveDir: OurMoveDirection) => {
      Rule(***, eval(surr), eval(moveDir), ***)
    }
  }
}
