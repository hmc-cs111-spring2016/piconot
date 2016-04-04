package piconot

import picolib.maze.Maze
import picolib.semantics._
import scala.collection.mutable._

package object external {
	def eval(program: Program): List[picolib.semantics.Rule] = {
	    val start = Rule(Name("@"), List( SubRule(Condition(), Action(next = Next(program.start)) )))
	    val program1 = Program(program.start, start::program.execs)
		val program2 = taskNametoStart(program1)
		
		val program3 = ruleNametoTask(program2)
		
		val ruleList = program3.execs.map({
			case t: Task => t.rules map {r => ruleToPicoRules(t.name.value+".", r)}
			case r: Rule => List(ruleToPicoRules("", r))

	    })
	    
	    ruleList.flatten.flatten

	}

	
	def ruleToPicoRules(taskName: String, rule: Rule): List[picolib.semantics.Rule] = {
	    rule.subRules map {sr => 
	        picolib.semantics.Rule(State(taskName+rule.name.value),
	                               conditionToSurroundings(sr.condition),
	                               actionToMoveDir(sr.action),
	                               (if (sr.action.next.name.value == "")
	                                    State(taskName+rule.name.value)
	                                else
	                                    State(sr.action.next.name.value))
	                              )}
	}
	
	def actionToMoveDir(action: Action): MoveDirection = {
	    action.move.direction match {
	        case x if x eq None  => StayHere
	        case x if x eq Up    => North
	        case x if x eq Down  => South
	        case x if x eq Left  => West
	        case x if x eq Right => East
	    }
	}
	
	def conditionToSurroundings(cond: Condition): Surroundings = {
	    var directions: MutableList[RelativeDescription] = MutableList(Anything, Anything, Anything, Anything)
	    cond.walls.directions.foreach { d => 
	        if (d eq Up)
	            directions(0) = Blocked
	        else if (d eq Right)
	            directions(1) = Blocked
	        else if (d eq Left)
	            directions(2) = Blocked
	        else if (d eq Down)
	            directions(3) = Blocked
	    }
	    
	    cond.empties.directions.foreach { d =>
	        if (d eq Up)
	            directions(0) = Open
	        else if (d eq Right)
	            directions(1) = Open
	        else if (d eq Left)
	            directions(2) = Open
	        else if (d eq Down)
	            directions(3) = Open
	    }
	    
	    Surroundings(directions(0), directions(1), directions(2), directions(3))
	}
	
	// Replaces calls to rules inside a task with taskName.ruleName
	// Leaves calls to outside rules the same
	def ruleNametoTask(program: Program): Program = {
	    val newExecs = program.execs.map({
			case t: Task => {
			    val changes = t.rules map {r => (r.name, Name(t.name.value+"."+r.name.value))}
			    val newRules = t.rules map {r => replaceNextNames(r, changes)}
			    Task(t.name, t.start, newRules)
			}
			case r: Rule => r
		})
		Program(program.start, newExecs)
	}
	
	// replace all of the Nexts in a program that correspond to a task with
	// taskName.taskStart"""
	def taskNametoStart(program: Program): Program = {
	   
		val taskNametoStart = program.execs.map({
			case x: Task => (x.name, Name(x.name.value +"."+ x.start.value)) //task names to start name
			case x: Rule => (x.name, x.name)                     //outer rule names stay the same
		})
		
		val newExecs = program.execs.map({
			case t: Task => Task(t.name, t.start, t.rules map {r => replaceNextNames(r, taskNametoStart)})
			case r: Rule => replaceNextNames(r, taskNametoStart) })
		Program(program.start, newExecs)
	}
	
	// replace all of the Nexts in a rule that correspond to a value in
	// changes with that value
	def replaceNextNames(rule: Rule, changes: List[(Name, Name)]): Rule = {
	    val subRules = rule.subRules.map {sr => SubRule(sr.condition, 
	                                                    Action(sr.action.move, 
	                                                           Next(replaceName(sr.action.next.name, changes))))}
	    Rule(rule.name, subRules)
	}
	
	def replaceName(name: Name, changes: List[(Name, Name)]): Name = {
	    
	    if (changes.isEmpty){
	        //println(name.value+"EMPTY")  
	        Name(name.value)
	    } else
    	    if(changes.head._1 == name){
    	        //println(name.value+ " to " + changes.head._2.value) 
    	        changes.head._2
    	    } else
                replaceName(name, changes.tail)
	}

	def tasktoRules(task: Task): List[picolib.semantics.Rule] = {
	    null
	}
	def ruletoRules(rule: Rule): List[picolib.semantics.Rule] = {
	    null
	}

}