## Project Description 

Sample Prototype of an API that works like a bank system with users.

## Project Design 

N-tier architecture

##TODO
-[X] Open Account API(POST)
 
-[X] Deposit API (POST )

-[x] Withdraw API (POST)

-[x] Transfer API (PATCH)

-[x] Business Rules Implementation (Logic)

##H2 Database
 http://localhost:8080/h2-console

###Run application
- Testing (run tests)
     
            mvn clean test

- Building package(jar)

        mvn clean install

- Run application

       mvn spring-bootrun

 ## Endpoints

are defined here
            
  http://localhost:8080/swagger-ui.html

###In live environment the following features will be implemented
- capturing of customer details for KYC
- all user details validation
- A separate database for transaction logs will be used
