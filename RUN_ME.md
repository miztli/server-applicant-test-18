# My Taxi - REST API evaluation

Evaluation presented by Miztli Melgoza applying for the JAVA developer position.

### Getting Started

These instructions wil guide you through a basic project setup and will provide examples on how to execute REST API LIVE tests.

### Prerequisites

- [JDK 1.8+](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html)
- [MVN 3+](https://maven.apache.org/download.cgi)

### Executing automatic REST API LIVE tests.
JUNIT + Restassured + maven-failsafe-plugin

1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute the following command. `mvn verify`
3. Test reports will be generated in the following directories:
    - ***${root}/target/surefire-reports/surefire-reports/***
    - ***${root}/target/surefire-reports/surefire-reports/***

*Tests source can be found in ${root}/src/test/java/com/mytaxi*

### Compiling the project
1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute the following command. `mvn clean package`
3. Compile without running tests, execute:  `mvn clean package -DskipTests`

### Running the project
*NOTE: if you haven't compiled the project already, please, go one step back.*
1. Using your favorite shell, navigate to the root of the project, to find ***${root}/pom.xml*** 
2. Execute either of the following commands. 
    
    ```java -jar target/mytaxi_server_applicant_test-1.0.0-SNAPSHOT.jar```

    ```mvn spring-boot:run```
## Resource location
Manual REST API tests can be performed following the next resources table:
* *By default we use only JSON media types*

| RESOURCE      | HTTP METHOD     | DESCRIPTION                     | URL                           |  HEADERS                      |  QUERY PARAMS                                | BODY   | SUCCESS              | FAILURE             |
| ------------- | --------------- | ------------------------------- | ----------------------------- | ----------------------------- | -------------------------------------------- | -------| -------------------- | ------------------- |
| login         | POST            | authenticate user               | /login                        | Authorization: Bearer {token} | -------------------------------------------- | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| drivers       | GET             | find all drivers                | /v1/drivers                   | Authorization: Bearer {token} | -------------------------------------------- | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| drivers       | GET             | find driver by id               | /v1/drivers/{id}              | Authorization: Bearer {token} | -------------------------------------------- | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| drivers       | GET             | find driver by onlineStatus     | /v1/drivers/{id}              | Authorization: Bearer {token} | (String) onlineStatus={ONLINE, OFFLINE}      | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| drivers       | POST            | create new driver               | /v1/drivers                   | Authorization: Bearer {token} | -------------------------------------------- | {"username":"Miztli Melgoza", "password":"abcd1234"} | HTTP.CREATED(201) | HTTP.CONFLICT(409) |
|               |                 |                                 |                               |                               |                                              |                          |              |                     |
| drivers       | DELETE          | delete driver by id             | /v1/drivers/{id}              | Authorization: Bearer {token} | -------------------------------------------- | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
| drivers       | PUT             | update a driver's location      | /v1/drivers/{id}              | Authorization: Bearer {token} | (Float) longitude={x.y}                      | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
|               |                 |                                 |                               |                               | (Float) latitude={-y.x}                      | ------ |                      |                     |
| drivers       | PUT             | update a driver's online status | /v1/drivers/{id}             | Authorization: Bearer {token} | (String) onlineStatus={ONLINE, OFFLINE}      | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
| drivers       | PATCH           | update only necessary fields    | /v1/drivers/{id}              | Authorization: Bearer {token} | (String) onlineStatus={ONLINE, OFFLINE}      | ------ | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
|               | (not impl, yet) |                                 |                               |                               | (Float) longitude={x.y}                      | ------ |                      |                     |
|               |                 |                                 |                               |                               | (Float) latitude={-y.x}                      | ------ |                      |                     |
|               |                 |                                 |                               |                               | (String) username={''}                       | ------ |                      |                     |
| drivers       | GET             | search for a driver             | /v1/drivers/search            | Authorization: Bearer {token} | (String) username={''}                       | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
|               |                 |                                 |                               |                               | (String) onlineStatus={ONLINE, OFFLINE}      | ------ |                      |                     |
|               |                 |                                 |                               |                               | (Integer) rating={x}                         | ------ |                      |                     |
|               |                 |                                 |                               |                               | (String) licensePlate={''}                   | ------ |                      |                     |
|               |                 |                                 |                               |                               | (Integer) seatCount={x}                      | ------ |                      |                     |
|               |                 |                                 |                               |                               | (String) engineType={ELECTRIC,GAS,HYBRID}    | ------ |                      |                     |
|               |                 |                                 |                               |                               | (Boolean) convertible={true, false}          | ------ |                      |                     |
|               |                 |                                 |                               |                               | (String) manufacturerName={}                 | ------ |                      |                     |
| cars          | GET             | find a car assigned to a driver | /v1/drivers/{driverId}/car    | Authorization: Bearer {token} |                                              | ------ | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| cars          | GET             | find car by id                  | /v1/cars/{id}                 | Authorization: Bearer {token} | -------------------------------------------- | { "licensePlate":"MEHM", "convertible":true, "rating":9, "engineType":"HYBRID"} | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |
| cars          | POST            | create new driver               | /v1/cars                      | Authorization: Bearer {token} | -------------------------------------------- | {"username":"Miztli Melgoza", "password":"abcd1234"} | HTTP.CREATED(201) | HTTP.CONFLICT(409) |
| cars          | DELETE          | delete car by id                | /v1/cars/{id}                 | Authorization: Bearer {token} | -------------------------------------------- | ---- - | HTTP.NO_CONTENT(204) | HTTP.NOT_FOUND(404) |
| manufacturers | GET             | find a car's manufacturer       | /v1/cars/{carId}/manufacturers| Authorization: Bearer {token} | -------------------------------------------- | ---- - | HTTP.OK(200)         | HTTP.NOT_FOUND(404) |

## Built with
- [Springboot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org)
- [H2](http://www.h2database.com/html/main.html)
- [JWT](https://github.com/jwtk/jjwt)

## Author
- ***Miztli Melgoza***

## Acknowledgments
- It took me about 6 hours to complete the entire exercise.
- Just a few changes were added to the pre existing code.
- Although I could have implemented stuff as pagination, ordering, unit and integration tests with spock or a more robust authentication method, I tried to focus on the simplicity and the abstraction of the code to easily accomplish the tasks.
- If I failed to accomplish a task either by complete or partially, please, let me know in orther to finish it.

***Kind, regards.***