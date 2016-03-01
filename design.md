# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?
The target for the design is users who want to program Picobot and have either a basic knowledge of 
programming or logic flow.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?
We converted Picobot's implicit conditionals into explicit 'if' statements which programmers will be familiar
with and non-programmers may find closer to natural language and thus more readable. We also made the movememnts
more explicit by forcing the user to type 'move' and a direction when specifying a move. We think this will
better distinguish an action taken by the robot from a conditional check of the robot's surroundings. We created funtion names such as 'isEmpty(Direction)' and 'isWall(Direction)' to clarify how the robot checks its surroundings.
Our overall design principle is valuing code clarity and readability over brevity so the language is more 
accessible to novice programmers. We also chose to include task which set an initial state and allow the user
to group rules together. We think this will allow users to write more modular code by grouping related behaviors.  

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?
We think it's easier to express the difference between a bot's move and a conditional check of its surroundings. 
In the original code they both simply used N, E, W, S letters for both and the only distinguishing factor was the
placement of those letters in the code. In our code the syntax is different for each behavior. 

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?
Our syntax somewhat abstracts the idea of states and state machines so it could be less intuitive for an 
expert in that area. However, we think that even for that type of user having string names for states instead of
integers will make the code more readable. We did trade brevity for clarity so the code will be much longer 
and more repetitive for a given program so a Picobot expert would spend more time coding in our syntax than the 
original syntax.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?
8. We are changing all the keywords and symbols but the code still has the same general structure of conditional
-> movement based on that conditional.

## Is there anything you would improve about your design?
It would be helpful if there were a better way to make sure you are checking every direction of the surroundings
that you intend to. In the original syntax, you had to type exactly 4 characters and give a value to each direction. 
In our syntax you can leave directions completely unspecified. 


