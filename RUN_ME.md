# My Taxi - REST API evaluation

Evaluation presented by Miztli Melgoza applying for the JAVA developer position.

***Author: Miztli Melgoza***

### Getting Started

These instructions wil guide you through a basic project setup and will provide examples on how to execute REST API LIVE tests.

### Prerequisites

- [JDK 1.8+](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html)
- [MVN 3+] (https://maven.apache.org/download.cgi)

### Executing automatic REST API LIVE tests.
JUNIT + Restassured + maven-failsafe-plugin

1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute the following command. `mvn verify`
3. Test reports will be generated in the following directories:
    - * ***${root}***/target/surefire-reports/surefire-reports/*
    - * ***${root}***/target/surefire-reports/surefire-reports/*

*Tests source can be found in ${root}/src/test/java/com/mytaxi*

### Compiling the project
1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute the following command. `mvn clean package`
3. Compile without running tests, execute:  `mvn clean package -DskipTests`
### Running the project
*NOTE: if you haven't compiled the project already, please, go one step back.*
1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute the following command. `java -jar target/`

## Resource location
Manual REST API tests can be performed following the next resources table:

| RESOURCE    | HTTP METHOD | DESCRIPTION      | URL         |  HEADERS |  QUERY PARAMS  | BODY   | SUCCESS      | FAILURE             |
| ----------- | ----------- | ---------------- | ----------- | ------ | ------------ | ------------------- |
| drivers     | GET         | find all drivers | /drivers    | ------ | ------ | ------ | HTTP.OK(200) | HTTP.NOT_FOUND(404) |
| drivers     | GET         | find driver by id | /drivers/{id} ------ | ------ | ------ | HTTP.OK(200) | HTTP.NOT_FOUND(404) |
| drivers     | POST         | create new driver | /drivers    | ------ | ------ | {"username":"Miztli Melgoza", "password":"abcd1234"} | HTTP.CREATED(201) | HTTP.CONFLICT(409) |
| drivers     | DELETE         | delete driver by id | /drivers/{id}    | ------ | ------ | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
| drivers     | PUT         | update a driver location | /drivers/{id}    | ------ | ------ | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |


```

### Http requests with httpie

    CARS
    http GET http://localhost:8080/v1/cars/4
    http GET http://localhost:8080/v1/cars
    http POST http://localhost:8080/v1/cars licensePlate=ABCD-232 seatCount:=4 convertible:=false engineType=GAS
    http DELETE http://localhost:8080/v1/cars/3

    DRIVERS
    http GET http://localhost:8080/v1/drivers?onlineStatus=ONLINE
    http GET http://localhost:8080/v1/drivers?onlineStatus=OFFLINE
    http PUT http://localhost:8080/v1/drivers/1?carId=1&selected=true

### Start the application

    With maven plugin: mvn spring-boot:run
    As packaged app: 
        mvn clean package
        java -jar target/mytaxi_server_applicant_test-1.0.0-SNAPSHOT.jar