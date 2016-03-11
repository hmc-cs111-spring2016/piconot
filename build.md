To build and run, run the following command at the top level directory:

sbt "run-main piconot.external.Program (maze filepath) (program filepath)"

To build and run the empty maze program, run:

sbt "run-main piconot.external.Program resources/empty.txt src/main/scala/piconot/external/Empty.bot"