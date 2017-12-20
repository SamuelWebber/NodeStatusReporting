The java program has been created with java jdk1.8.0_121. So should work in Java jdk1.8.0

In order to ensure that the program can be compiled, please ensure that a path to the bin directory of the jdk1.8.0_121 has been set up.
To do this go to This PC, Properties, Change Settings, Advanced, Environment Variables.... Then click new under User variables and set up
the new path variable, with the path to ../jdk1.8.0_121/bin.

To compile the file navigate to the .java file directory in command line, then compile: javac NodeStatusReporting.java

To run the program: java NodeStatusReporting input.txt 
(an example input.txt file has been included) 