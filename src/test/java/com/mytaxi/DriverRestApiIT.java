package com.mytaxi;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertTrue;

/**
 * This class handles REST API calls to Drive resources.
 */
public class DriverRestApiIT extends AbstractRestAssuredAPITest{

    /**
     * CREATE driver
     * @throws JSONException
     */
    @Test
    public void createValidUser_then_validateFields() throws JSONException {
        createDriverByAPI("test-driver-1000", "test-password-1000")
              .assertThat()
                 .body(FIELD_ID, notNullValue())
                 .body(FIELD_USERNAME, notNullValue())
                 .body(FIELD_PASSWORD, notNullValue());
    }

    /**
     * FIND driver by id
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
     * FIND all drivers
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
                        .getInt(FIELD_ID);

        String testUsername2 = "test-driver-2222";
        String testPassword2 = "test-password-2222";
        int createdId2 =
                createDriverByAPI(testUsername2, testPassword2)
                        .extract()
                        .jsonPath()
                        .getInt(FIELD_ID);

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
                        Integer id = (Integer) driver.get(FIELD_ID);
                        String name = (String) driver.get(FIELD_USERNAME);
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
                            Integer id = (Integer) driver.get(FIELD_ID);
                            String name = (String) driver.get(FIELD_USERNAME);
                            String password = (String) driver.get("password");
                            return name.equals(testUsername2) &&
                                    password.equals(testPassword2) &&
                                    id != 0;
                        })
                        .findFirst();
        assertTrue(foundCreated2.isPresent());
    }

    /**
     * DELETE driver by id
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
                        .getInt(FIELD_ID);

        //find by id and validate fields
        findDriverByIdAndValidate(createdId1, testUsername1, testPassword1);

        //delete driver
        deleteResource(DRIVERS_URL + "/" + createdId1);

        //find driver and expect not found
        given()
                .header(HEADER_AUTHORIZATION, authenticated())
        .get(DRIVERS_URL + "/" + createdId1)
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
                        .getInt(FIELD_ID);

        assertTrue(createdId1>0);

        // update driver location
        given()
                .header(HEADER_AUTHORIZATION, authenticated())
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
                        .getInt(FIELD_ID);

        assertTrue(createdId1>0);

        // create a driver 2
        int createdId2 =
                createDriverByAPI(secondDriverUsername, secondDriverPassword)
                        .extract()
                        .jsonPath()
                        .getInt(FIELD_ID);

        assertTrue(createdId2>0);

        //update drivers status
        given()
            .header(HEADER_AUTHORIZATION, authenticated())
            .param(FIELD_ONLINE_STATUS, "ONLINE")
        .put(DRIVERS_URL + "/" + createdId1)
            .then()
            .statusCode(204); //expect NO_CONTENT

        given()
            .header(HEADER_AUTHORIZATION, authenticated())
            .param(FIELD_ONLINE_STATUS, "ONLINE")
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
                .getInt(FIELD_ID);

        // select car for driver 1
        given()
                .header(HEADER_AUTHORIZATION, authenticated())
                .param("carId", carId)
                .param("selected", true)
                .put(DRIVERS_URL + "/" + createdId1)
                .then()
                .statusCode(204);

        // select car for driver 2 and expect CONFLICT
        given()
                .header(HEADER_AUTHORIZATION, authenticated())
                .param("carId", carId)
                .param("selected", true)
                .put(DRIVERS_URL + "/" + createdId2)
                .then()
                .statusCode(409);
    }

    /**
     * Search for drivers by criteria.
     * username - like (%match)
     * engineType - equals (match)
     * onlineStatus - equals (match)
     * licensePlate - like (%match)
     * seatCount - equals (match)
     * convertible - equals (match)
     * manufacturerName - like (%match)
     * @throws JSONException
     */
    @Test
    public void findMatchingDrivers_withAllFilters_then_ValidateFields() throws JSONException {
        String username = "driver";
        String engineType = "HYBRID";
        String onlineStatus = "ONLINE";
        String licensePlate = "ZAF";
        int seatCount = 5;
        boolean convertible = false;
        String manufacturerName = "AUDI";

        JsonPath jsonPath =
            given()
                    .header(HEADER_AUTHORIZATION, authenticated())
                    .param(FIELD_USERNAME, username)
                    .param(FIELD_ENGINE_TYPE, engineType)
                    .param(FIELD_ONLINE_STATUS, onlineStatus)
                    .param(FIELD_LICENSE_PLATE, licensePlate)
                    .param(FIELD_SEAT_COUNT, seatCount)
                    .param(FIELD_CONVERTIBLE, convertible)
                    .param(FIELD_MANUFACTURER_NAME, manufacturerName)
            .get(DRIVERS_URL + "/" + SEARCH_URL)
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath();

        List<Map<String, Object>> matchingDrivers = jsonPath.getList("");

        // compare in drivers list
        Optional<Map<String, Object>> driver =
                matchingDrivers
                        .stream()
                        .filter(d -> {
                            //driver fields
                            String foundUsername = (String) d.get(FIELD_USERNAME);
                            String foundOnlineStatus = (String) d.get(FIELD_ONLINE_STATUS);
                            //car fields
                            Map<String, Object> foundCar = (Map<String, Object>) d.get("carDTO");
                            String foundEngineType = (String) foundCar.get(FIELD_ENGINE_TYPE);
                            String foundLicencePlate = (String) foundCar.get(FIELD_LICENSE_PLATE);
                            int foundSeatCount = (int) foundCar.get(FIELD_SEAT_COUNT);
                            boolean foundConvertible = (boolean) foundCar.get(FIELD_CONVERTIBLE);
                            //manufacturer fields
                            Map<String, Object> foundManufacturer =
                                    (Map<String, Object>) foundCar.get(FIELD_MANUFACTURER_DTO);
                            String foundManufacturerName = (String) foundManufacturer.get(FIELD_NAME);

                            return foundUsername.contains(username) && //query uses like %s
                                   foundEngineType.equals(engineType) &&
                                   foundOnlineStatus.equals(onlineStatus) &&
                                   foundLicencePlate.contains(licensePlate) && //query uses like %s
                                   foundSeatCount == seatCount &&
                                   foundConvertible == convertible &&
                                   foundManufacturerName.contains(manufacturerName) ; //query uses like %s
                        }).findFirst();

        //validate matching driver
        assertTrue(driver.isPresent());
    }

    /**
     * Helper method.
     * Create the Driver JSON object
     * @param username The driver's name
     * @param password The driver's password
     * @return The HTTP Response
     * @throws JSONException
     */
    private ValidatableResponse createDriverByAPI(String username, String password) throws JSONException {
        JSONObject newDriver = new JSONObject()
                .put(FIELD_USERNAME, username)
                .put(FIELD_PASSWORD, password);

        return createResource(DRIVERS_URL, newDriver);
    }

    /**
     * Helper method.
     * @param licensePlate
     * @param seatCount
     * @param convertible
     * @param rating
     * @param engineType
     * @return
     * @throws JSONException
     */
    private ValidatableResponse createCarByAPI(String licensePlate,
                                               Integer seatCount,
                                               Boolean convertible,
                                               Integer rating,
                                               String engineType) throws JSONException {
        JSONObject cewCar = new JSONObject()
                .put(FIELD_LICENSE_PLATE, licensePlate)
                .put(FIELD_SEAT_COUNT, seatCount)
                .put(FIELD_CONVERTIBLE, convertible)
                .put(FIELD_RATING, rating)
                .put(FIELD_ENGINE_TYPE, engineType);

        return createResource(CARS_URL, cewCar);
    }

    /**
     * Helper method.
     * Find a driver and validate response fields
     * @param id The driver's d
     * @param username The driver's name
     * @param password The driver's password
     * @return
     */
    private ValidatableResponse findDriverByIdAndValidate(int id, String username, String password) throws JSONException {
        return findDriverById(id)
                .assertThat()
                .body(FIELD_ID, equalTo(id))
                .body(FIELD_USERNAME, equalTo(username))
                .body(FIELD_PASSWORD, equalTo(password));
    }

    /**
     * Helper method.
     * Find a driver via REST API
     * @param id The driver's id
     * @return The HTTP response
     */
    private ValidatableResponse findDriverById(int id) throws JSONException {
        return findResource(DRIVERS_URL + "/" + id);
    }
}
