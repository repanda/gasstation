# Requirements
- Maven 3 (using mvn 3.10.1 version)
- Java 17 (using jdk-17.0.1)

# Build
mvn clean install

# Run
The precedent command will lunch the tests, also you can run tests using 'mvn test' or in your favorite IDE 


# Multithreading
The idea would be only a single thread owning all writes
to a single resource with other threads reading the results. 

In other words we have one producer generates customer request to buy gas
and multiple consumers that handles customer's request based on the pump type.

This approach helps us separate business logic and rules from technical issues
dealing with multithreading.
Also, we delegate concurrent reads and writes to be handled by external components
(ConcurrentMap, ConcurrentLinkedQueue). 
