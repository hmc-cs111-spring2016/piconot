# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

We assume that users will have familiarity with the Picobot world and basic computer science concepts. That is, while
we expect the users will have understanding of the Picobot simulation and state machines, we do not expect the 
users will have knowledge of advanced programming and computer science. Ideally, students with a CS5 level of
eductation should be able to work with this language design.


## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

While our language design is somewhat similar to the original picobot language, we feel our language design improves upon
lacking aspects of the original design. Since we saw a majority of good "readable" picobot code groups the same state
lines together, we require all states to be grouped together under a keyword. This both saves time coding (by preventing
duplicate coding) and enforcing good organization style. Additionally, by using unicode characters, we feel it is more
intuitive and visually understandable. Furthermore, we chose to remove the ordering requirement from the arrows. Even 
though the original NEWS ordering was simple enough (due to the acronymy), removing this forced restriction improves
expression. Additionally, we list the Open arrows second since it makes it closer to the move direction arrow. This makes
sense since if the Move Direction needs to be in the Open set to ensure no errors.


## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

It is easier to cohesively group the behaviors for a state. As such, the user can easily see at a glance what partial rules they have already defined for a state and how the partial rules work together. With the old design, states did not have to be grouped, which made reading the code itself more difficult. It also means a little less typing as you don't have to specify the original state each time. Additionally using unicode arrows increases readability.


## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

The potentially most complex part is specifying which directions are currently blocked and unblocked. In the original
design, the user input a string in NEWS order, where a capital letter meant block, a star meant must be open, and an x
meant anything could pass. This specified ordering made the input very regular each time. However, in our new design, the
user has to specify blocked and unblocked seperately. This could cause some confusion.


## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

4 -- We are pretty close to the original design, but we have made a couple of significant syntax changes.

## Is there anything you would improve about your design?

This language is really better suited to something more gui-based because of the cardinal directions. Viewing and writing these would be easier to undersand through some sort of graphic, like a compass with directions highlighted. Otherwise, we like the format of grouping the rules by associated state. We also thought that the format of "current conditions go to new conditions" was very clear, so we did not see a reason to change that.
