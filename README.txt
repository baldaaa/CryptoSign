Project assignment:

 Your assignment is to implement REST API using a JAVA service, that provide
 cryptographic sign functionalities.

 API:
 1.Generate new RSA key pair
    Return Value: Key ID

 2.Delete an existing RSA key pair
    Parameters: Key ID


 3.Sign on a given data using the given key
    Parameters: Key ID, Data
    Return Value: Signature


 4.Verify the given signature on the given data using the given key
    Parameters: Key ID, Data, Signature
    Return value: True/False

 5.List all existing keys IDs
    Return value: List of keys IDs

Notes:
1.The service should return HTTP code for each operation
2.The service should handle errors (including bad input) and return the correct HTTP error code.
3.No need for persistency (no DB is required).



Implementation report:

Implemented as Spring-boot application with swagger2 enabled.

Runtime dependencies:
    Apache Maven 3.
    Java 1.11

How to run application:
1.Goto root project folder and execute bellow commands:
    mvn test - to execute integration tests
    mvn package - to compile, test and package application
    mvn spring-boot:run  - to run application
  Example: Katerinas-MBP:CryptoSign katia$ /LocalTools/apache-maven-3.5.3/bin/mvn spring-boot:run

Where to find REST API specification and try:
    when application is up
    visit swagger page at http://localhost:8080/swagger-ui.html

