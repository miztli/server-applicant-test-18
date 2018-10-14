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