# Load test REST API using Gatling-Java 

#### ==> Do not execute with more than one user and only one execution
#### ==> All the requests are made to reqres.in website 
##### ==> This website is not designed for load testing
#### ===> Enable one request at a time
##### This project is an extension UI application using Gatling https://github.com/gatling/gatling-maven-plugin-demo-java

#### Scenarios Covered
    1.  How to get an auth token using JSON path and pass it in another request headers
    2.  Call Get endpoint, capture response, and assert 
    3.  Write a response to the console to see the response
    4.  Capture response data into an array and execute until the array is empty
    5.  Call child thread /depend
    6.  Post request - Boday as String
    7.  Loops - forever, repeat, doWhile ..
    8.  Multiple endpoints in one scenario, using chainbuilder
    9.  Call the auth token endpoint once and repete other endpoints forever
    10. InjectOpen and InjectClose -difference
    11. If Assertion fails exit the loop 
    12. Assign users for each endpoint as needed 
    13. Print session data, using session data

#### Feeders 
    14. how to read data from files for post and get calls
    15. how to feed dynamic values from a CSV file
    16. how to create dynamic data for each request using custom feeders
            i.e using date, random number, UUID, names ..etc












>>>>>>> 4241804 (Initial Gatling Java testing)
>>>>>>> 5fb1669 (first commit)
