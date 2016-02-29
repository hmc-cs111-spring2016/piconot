package piconot.external.semantics

/**
 * This file adds semantic checks to Piconot, including:
 *    Checking for undefined states
 *    Checking for unreachable states
 *    Checking for a rule that doesn't allow a move ("boxed-in" states)
 *    Checking for a rule that moves into a wall
 *    
 * The checks are implemented as stacked traits that can be mixed into 
 * the `ErrorCollector` trait
 */

import picolib.semantics._

trait ErrorChecker[T] {
  def check(value: T): Seq[Error]
}

class DefaultChecker[T] extends ErrorChecker[T] {
  override def check(value: T) = Seq.empty[Error]
}

trait UndefinedStates extends ErrorChecker[Picobot] {
  abstract override def check(bot: Picobot) = {
    val startStates = bot.rules map (_.startState)
    val endStates = bot.rules map (_.endState)
    val undefined = endStates filterNot (startStates contains _)
    val newErrors = undefined map (s ⇒ new Error(f"Undefined state: ${s}"))
    super.check(bot) ++ newErrors
  }
}

trait UreachableStates extends ErrorChecker[Picobot] {
  abstract override def check(bot: Picobot) = {
    val startStates = (bot.rules map (_.startState)).tail // the first state is always reachable
    val endStates = bot.rules map (_.endState)
    val unreachable = startStates filterNot (endStates contains _)
    val newErrors = unreachable map (s ⇒ new Error(f"Unreachable state: ${s}"))
    super.check(bot) ++ newErrors
  }
}

trait BoxedIn extends ErrorChecker[Picobot] {
  abstract override def check(bot: Picobot) = {
    val blockedIn = bot.rules filter allBlocked
    val newErrors = blockedIn map (r ⇒ new Error(f"Inescapable rule: ${r}"))
    super.check(bot) ++ newErrors
  }
  
  def allBlocked(rule: Rule) = {
    val surroundings = rule.surroundings
    surroundings.north == Blocked && surroundings.east == Blocked &&
    surroundings.west == Blocked && surroundings.south == Blocked
  }
}

trait MoveToWall extends ErrorChecker[Picobot] {
  abstract override def check(bot: Picobot) = {
    val blockedIn = bot.rules filter movingToBlocked
    val newErrors = blockedIn map (r ⇒ 
        new Error(f"Can't move ${r.moveDirection } toward wall: ${r}"))
    super.check(bot) ++ newErrors
  }
  
  def movingToBlocked(rule: Rule) = {
    val inDirection: RelativeDescription = rule.moveDirection match {
      case North ⇒ rule.surroundings.north 
      case East ⇒ rule.surroundings.east 
      case West ⇒ rule.surroundings.west 
      case South ⇒ rule.surroundings.south
      case StayHere ⇒ Anything
    } 
    inDirection == Blocked
  }
}