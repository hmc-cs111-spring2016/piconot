# Building and running Piconot
You can compile and run code for the external dls using the command provided on the website.

`sbt "run-main piconot.external.Piconot resources/empty.txt src/main/scala/piconot/external/Empty.bot"`

It's easy to modify this command for different maps and code files. The following command runs the other example code file on the other provided map.

`sbt "run-main piconot.external.Piconot resources/maze.txt src/main/scala/piconot/external/RightHand.bot"`

You can also run programs written with the internal DSL via sbt. The following commands will run the two example programs for the internal DSL.

`sbt "run-main fillEmptyRoom"`

`sbt "run-main fillMaze"`


Since I didn't use Eclipse at all during this assignment, I have no idea how compatible it is with my implementations. However, since sbt works, I suspect that Eclipse will work as well.