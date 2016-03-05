# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

This design is targeted towards someone with enough programming experience to write simple functions. The goal of the design is to provide more familiar syntax to a programmer. It also allows the user to be more flexible in how they evaluate the current surroundings of Picobot. 

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

We thought that the `*` and `x` states were too confusing and that it was too tedious to enforce directional order when describing the surroundings of the bot. Thus, we decided to let users only express the surrounding states relevant to desired behavior without constraining the directional order. We also decided that being able to express multiple commands at once would be more convenient than the current Picobot's behavior of separating every command into a new state.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

The `x` direction is more intuitive in this design, especially to someone with a programming background who is more accustomed to boolean values. The `*` behavior is also easier to express because it is the default state of a direction when the user does not specify anything for that direction. Recursive behavior within a state is also easier to express in our design. We designed this functionality in a `while` command so that the user does not need to think about the problem in terms of repeatedly calling a certain state. Ideally this behavior is more semantically intuitive to the user. 

Also, its easier to input multiple commands in a single line, since we don't require a new state for each command. This allows the user to express more complex behavior more concisely.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

Simple programs are more difficult to express in our design than the original because our syntax is more verbose. The user would need to include the `move` and `while` commands in addition to simply describing start and end states.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

We think that our syntax would be an 8 on this scale. Though our syntax looks different and includes more intuitive commands, we chose not to greatly alter the abilities of Picobot beyond the appearance of the code. It's closer to the 'very different' end of the scale, since it lets users easily do things that would take multiple states in Picobot.

## Is there anything you would improve about your design?

At first, we wrote that we would like the user to be able to input a list of actions instead of single actions across multiple states, but we ended up deciding to implement this in our syntax. Other than this, we're happy with our design.
