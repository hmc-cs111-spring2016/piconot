# Design

For the most part, this syntax will be a more verbose but hopefully easier to remember version of the original picobot syntax. A given rule will be a (_state_; _action_) pair, where the _state_ is everything that would be to the left of the arrow in the original syntax (i.e. picobot's state and surroundings), and the _action_ is everything that would be to the right. Each piece of information will be prefaced with a label, so for example, the rule
```
0 xxxx -> N 0
```
will become

```
(State: 0, North: x, South: x, East: x, West: x; Go: North, NewState: 0)
```
While "no wall" should still be specified by `x`, since there are already labels there's no need to specify "wall" by a different letter for each direction; this syntax will use `w` (for "wall") instead. It should be possible to write the directions as either full words or just first letters (e.g. both "North" and "N" are acceptable). Additionally, in an ideal implementation, omitting certain pieces of information altogether would give default behavior. It doesn't make sense to ever omit the current state from a rule, but the other default behaviors would be as follows: if wall/no wall is not specified for some direction, default to wildcard; if movement direction is not specified, do not move; if the new state is not specified, stay in the old state. Thus the the rule
```
0 Nx** -> N 0
```
can be written most concisely as 
```
(State: 0, E: x, N: w; Go: N)
```
but it can also be written as
```
(State: 0, South: *, North: w, West: *, East: x; Go: North, NewState: 0)
```

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?
The target is generally speaking people who haven't programmed before. This is also picobot's target audience, but picobot is kind of a pain to use considering that's the case. I'm not assuming any special knowledge on the part of the users, beyond knowing how picobot itself works.

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?
This syntax still teaches the same concepts as picobot does, since it works more or less exactly the same way, but it's somewhat less strict. My hope is that this will allow beginners to get used to it more easily.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?
There are no behaviors that require fewer rules to express, because this is intended to have identical semantics to the original picobot.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?
There are also no behaviors that require more rules, for the same reason as above.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?
Maybe a 4 or so? It looks pretty different, but it's very much still recognizably picobot.

## Is there anything you would improve about your design?
