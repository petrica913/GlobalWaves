# Proiect GlobalWaves  - Etapa 3

<div align="center"><img src="https://tenor.com/view/listening-to-music-spongebob-gif-8009182.gif" width="300px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa3](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa3)

## Implementation
In making the third part of the homework I used my implementations from the second part.
Besides the Singleton pattern that I've implemented on the Admin class, at this part of
the homework I've implemented the Command, Factory and Visitor design patterns. 
I've used Visitor pattern in the implementation of the page system, because the Visitor
pattern separates the logic for displaying pages from the page classes themselves.
This promotes a clear separation of concerns, making the code more modular and easier 
to maintain. If you introduce new page types, you can simply create a new visitor 
implementation for the new types without modifying the existing visitor or page classes.
This makes it easy to extend your system with new features.

In the making of the statistics I've used the Factory design pattern because The client
code (the code that generates statistics) does not need to know the details of how each 
type of statistics object is created. It relies on the abstraction provided by the 
StatisticsFactory interface. This encapsulation allows for changes in the concrete 
classes (like adding new types of statistics) without affecting the client code. The use 
of a factory provides a clear and readable way to create instances of different statistics 
types based on the user's type.

In the making of the subscribe method I've used the Command design pattern becauseIn the
making of the subscribe method, I've used the Command design pattern because it provides 
a structured and flexible way to encapsulate the subscription operation. The Command
pattern proves valuable in managing and organizing complex user interactions, providing a
clear and structured approach to handling subscription-related operations.


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests and library in JSON format
* ref/ - contains all reference output for the tests in JSON format

