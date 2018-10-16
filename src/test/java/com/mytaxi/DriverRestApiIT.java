package com.mytaxi;

import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class DriverRestApiIT extends AbstractRestAssuredAPITest{

    /**
     * CREATE user
     * @throws JSONException
     */
    @Test
    public void createValidUser_then_validateFields() throws JSONException {
        createDriverByAPI("test-driver-1000", "test-password-1000")
              .assertThat()
                 .body("id", notNullValue())
                 .body("username", notNullValue())
                 .body("password", notNullValue());
    }

    /**
     * FIND by id
     * @throws JSONException
     */
    @Test
    public void createNewDriver_then_lookForCreatedIdViaAPI() throws JSONException {
        String testUsername = "test-driver-1001";
        String testPassword = "test-password-1001";
        int createdId =
          createDriverByAPI(testUsername, testPassword)
                .extract()
                .jsonPath()
                .getInt("id");

          findDriverByIdAndValidate(createdId, testUsername, testPassword);
    }

    /**
     * FIND all
     * @throws JSONException
     */
    @Test
    public void createTwoNewDrivers_then_getDriverListAndValidateFields() throws JSONException {
        String testUsername1 = "test-driver-1111";
        String testPassword1 = "test-password-1111";
        int createdId1 =
                createDriverByAPI(testUsername1, testPassword1)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        String testUsername2 = "test-driver-2222";
        String testPassword2 = "test-password-2222";
        int createdId2 =
                createDriverByAPI(testUsername2, testPassword2)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        //Check generated id's > 0
        assertTrue(createdId1>0);
        assertTrue(createdId2>0);

        List<Map<String, Object>> driverList = findResource(DRIVERS_URL)
                                    .extract()
                                    .jsonPath()
                                    .getList("");

        //Validate 1st created driver
        Optional<Map<String, Object>> foundCreated1 =
            driverList
                  .stream()
                  .filter( driver -> {
                        Integer id = (Integer) driver.get("id");
                        String name = (String) driver.get("username");
                        String password = (String) driver.get("password");
                        return name.equals(testUsername1) &&
                               password.equals(testPassword1) &&
                               id != 0;
                  })
                  .findFirst();
        assertTrue(foundCreated1.isPresent());

        //Validate 2nd created driver
        Optional<Map<String, Object>> foundCreated2 =
                driverList
                        .stream()
                        .filter( driver -> {
                            Integer id = (Integer) driver.get("id");
                            String name = (String) driver.get("username");
                            String password = (String) driver.get("password");
                            return name.equals(testUsername2) &&
                                    password.equals(testPassword2) &&
                                    id != 0;
                        })
                        .findFirst();
        assertTrue(foundCreated2.isPresent());
    }

    /**
     * DELETE by id
     * @throws JSONException
     */
    @Test
    public void createDriver_then_findById_then_deleteById_andValidateIsNotExistent() throws JSONException {
        String testUsername1 = "test-deleted-driver-1111";
        String testPassword1 = "test-deleted-password-1111";

        //create driver
        int createdId1 =
                createDriverByAPI(testUsername1, testPassword1)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        //find by id and validate fields
        findDriverByIdAndValidate(createdId1, testUsername1, testPassword1);

        //delete driver
        deleteResource(DRIVERS_URL + createdId1);

        //find driver and expect not found
        delete(DRIVERS_URL + createdId1)
                .then()
                .statusCode(404);
    }

    /**
     * UPDATE a driver location
     * @throws JSONException if the driver couldn't be created
     */
    @Test
    public void updateDriversLocation_then_validateUpdatedFields() throws JSONException {
        String testLatitude = "19.3963386";
        String testLongitude = "-99.11554";
        String testUsername1 = "test-updatable-driver-1111";
        String testPassword1 = "test-updatable-password-1111";

        //create a driver
        int createdId1 =
                createDriverByAPI(testUsername1, testPassword1)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        assertTrue(createdId1>0);

        // update driver location
        given()
                .param("latitude", testLatitude)
                .param("longitude", testLongitude)
        .put(DRIVERS_URL + "/" + createdId1)
                .then()
                .statusCode(204);

        // find updated driver
        ValidatableResponse response =
                findDriverByIdAndValidate(createdId1, testUsername1, testPassword1);

        Map<String, Object> coordinate =
                                response.extract()
                                  .jsonPath()
                                  .getMap("coordinate");

        Float updatedLatitude = (Float) coordinate.get("latitude");
        Float updatedLongitude = (Float) coordinate.get("longitude");

        //validate updated values
        assertTrue(Float.valueOf(testLatitude).equals(updatedLatitude));
        assertTrue(Float.valueOf(testLongitude).equals(updatedLongitude));
    }

    /**
     * Select a car for an ONLINE driver, check if thar car is
     * assigned to the same driver.
     * Then, try assigning the same car to another ONLINE driver
     * and expect error message (409).
     * Finally, deselect car.
     */
    @Test
    public void selectACarForADriver_then_lookForAssignatedCar_then_selectForOtherDriver_finally_DeselectCar() throws JSONException {
        String firstDriverUsername = "test-selectable-driver-1111";
        String firstDriverPassword = "test-selectable-password-1111";
        String secondDriverUsername = "test-selectable-driver-2222";
        String secondDriverPassword = "test-selectable-password-2222";

        // create a driver 1
        int createdId1 =
                createDriverByAPI(firstDriverUsername, firstDriverPassword)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        assertTrue(createdId1>0);

        // create a driver 2
        int createdId2 =
                createDriverByAPI(secondDriverUsername, secondDriverPassword)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        assertTrue(createdId2>0);

        //update drivers status
        given()
            .param("onlineStatus", "ONLINE")
        .put(DRIVERS_URL + "/" + createdId1)
            .then()
            .statusCode(204); //expect NO_CONTENT

        given()
            .param("onlineStatus", "ONLINE")
        .put(DRIVERS_URL + "/" + createdId2)
            .then()
            .statusCode(204); //expect NO_CONTENT

        //create a car
        String licensePlate = "TEST-PLATE";
        int seatCount = 5;
        boolean convertible = false;
        int rating = 5;
        String engineType = "ELECTRIC";

        int carId =
            createCarByAPI(licensePlate, seatCount, convertible, rating, engineType)
                .extract()
                .jsonPath()
                .getInt("id");

        // select car for driver 1
        given()
                .param("carId", carId)
                .param("selected", true)
                .put(DRIVERS_URL + "/" + createdId1)
                .then()
                .statusCode(204);

        // select car for driver 2 and expect CONFLICT
        given()
                .param("carId", carId)
                .param("selected", true)
                .put(DRIVERS_URL + "/" + createdId2)
                .then()
                .statusCode(409);
    }

    /**
     * Create the Driver JSON object
     * @param username The driver's name
     * @param password The driver's password
     * @return The HTTP Response
     * @throws JSONException
     */
    private ValidatableResponse createDriverByAPI(String username, String password) throws JSONException {
        JSONObject newDriver = new JSONObject()
                .put("username", username)
                .put("password", password);

        return createResource(DRIVERS_URL, newDriver);
    }

    private ValidatableResponse createCarByAPI(String licensePlate,
                                               Integer seatCount,
                                               Boolean convertible,
                                               Integer rating,
                                               String engineType) throws JSONException {
        JSONObject cewCar = new JSONObject()
                .put("licensePlate", licensePlate)
                .put("seatCount", seatCount)
                .put("convertible", convertible)
                .put("rating", rating)
                .put("engineType", engineType);

        return createResource(CARS_URL, cewCar);
    }

    /**
     * Find a driver and validate response fields
     * @param id The driver's d
     * @param username The driver's name
     * @param password The driver's password
     * @return
     */
    private ValidatableResponse findDriverByIdAndValidate(int id, String username, String password) {
        return findDriverById(id)
                .assertThat()
                .body("id", equalTo(id))
                .body("username", equalTo(username))
                .body("password", equalTo(password));
    }

    /**
     * Find a driver via REST API
     * @param id The driver's id
     * @return The HTTP response
     */
    private ValidatableResponse findDriverById(int id) {
        return findResource(DRIVERS_URL + "/" + id);
    }
}
