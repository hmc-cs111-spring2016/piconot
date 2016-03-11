# Building and running Piconot
You can compile and run code using the command provided on the website.

`sbt "run-main piconot.external.Piconot resources/empty.txt src/main/scala/piconot/external/Empty.bot"`

It's easy to modify this for different maps and code files. The following command runs the other example code file on the other provided map.

`sbt "run-main piconot.external.Piconot resources/maze.txt src/main/scala/piconot/external/RightHand.bot"`

Since I didn't use Eclipse at all during this assignment, I have no idea how compatible it is with my external DSL. However, since sbt works, I suspect that Eclipse will work as well.