#random

Build a clone of $ cat /dev/random generating your own entropy. 

## Setup, Installation, and running the application:

1 - Install Java 8

2 - Install Maven 3.6.3 or higher

At project's root directory, perform the following commands:
 
3 - mvn clean package 

Find dev.random.jar generated within project/target directory
 
Ways to run the program:
 
java -jar dev.random.jar
  
java -jar dev.random.jar 100 uint16
  
java -jar dev.random.jar 10 uint8

