# Design

For the most part, this syntax will be a more verbose but hopefully easier to remember version of the original picobot syntax. A given rule will be a (_state_, _action_) pair, where the _state_ is everything that would be to the left of the arrow in the original syntax (i.e. picobot's state and surroundings), and the _action_ is everything that would be to the right. Each piece of information will be prefaced with a label, so for example, the rule
```
0 xxxx -> N 0
```
will become

```
(State: 0 North: x South: x East: x West: x, Go: North NewState: 0)
```

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

## Is there anything you would improve about your design?
