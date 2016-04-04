# Building and running Piconot

In the piconot directory, run the following command:

`sbt "run-main piconot.external.Piconot MAP_FILE EXTERNAL_PROGRAM"`

where MAP_FILE is the name of the map layout for our Piconot to solve (i.e. resources/maze.txt) and EXTERNAL_PROGRAM is the name of the Piconot bot program written in our external DSL (i.e. src/main/scala/piconot/external/RightHand.bot). 

Thus, if run with the command:

`sbt "run-main piconot.external.Piconot resources/empty.txt src/main/scala/piconot/external/Empty.bot"`

the program will run a bot that can solve the empty maze.
