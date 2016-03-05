# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

Our goal is to make the syntax less intimidating for computer novices. We want to turn picobot from a coding problem into a logic puzzle, where instead of writing code that controls the robot, it feels more like users are writing instructions. We're trying to assume as little knowledge as possible on the part of the user.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

By removing the code flavor, we expand the domain of maze-searching to something that those without a coding background can do. The hope is that writing a program in this language doesn't feel like "coding" in a traditional sense. Maze navigation has problems in it that aren't necessarily best solved by the kind of mindset that you'd have if you feel you're writing code or a program -- e.g., repeating a pattern over and over (but not necessarily thinking of loops or states). In particular, we want to get away from the idea of states which is so deeply a part of picobot. We've essentially replaced states with goto statements.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

In theory, the behaviours that are needed to solve these puzzle should be inherently easier to express. Firstly, the code is read sequentially, which we believe is easier to think about than states and rules. Additionally, the user is only expected to provide the necessary information at each line, i.e. only the state conditions that are blocked or open (not "anything"). We also hope that the use of natural language is appropriate for different audiences than the original picobot design.

Essentially, our modification makes a set of commands read like a script or set of instructions, which we hope is more intuitive to novice programmers.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

This might be obvious, but anything where you would want to rely on states heavily would now be much harder. Complicated conditional behaviours (i.e., go to state a if x, state b if y, etc.) are now not possible, and though they would be if we added the functionality described below, we still think it would be more difficult.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

7.

## Is there anything you would improve about your design?

Goto statements are kind of bad - that said, we think that to someone who hasn't seen code before or who doesn't think in terms of state machines, it might be easier to think about the code in terms of goto statements. We do wish we didn't have the problems associated with goto statements.

We also understand that the current syntax only really allows for a single path through the code. We have ideas for extending it to prevent this, like adding if-else statements or saying to go to a tag a certain number of times. However, we do not believe these are necessary to solve the empty room or maze.