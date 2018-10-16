package com.mytaxi;

import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.delete;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CarRestApiIT extends AbstractRestAssuredAPITest {

    /**
     * FIND by id
     * @throws JSONException
     */
    @Test
    public void createNewCar_then_lookForCreatedIdViaAPI() throws JSONException {
        String licensePlate = "TEST-PLATE-H";
        int seatCount = 4;
        boolean convertible = true;
        int rating = 10;
        String engineType = "HYBRID";

        // CREATE car
        int carId =
                createCarByAPI(licensePlate, seatCount, convertible, rating, engineType)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        assertTrue(carId>0);

        //find by API
        findCarByIdAndValidate(carId, licensePlate, seatCount,
                               convertible, rating, engineType);
    }

    /**
     * Create a CAR via REST API
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
                .put("licensePlate", licensePlate)
                .put("seatCount", seatCount)
                .put("convertible", convertible)
                .put("rating", rating)
                .put("engineType", engineType);

        return createResource(CARS_URL, cewCar);
    }

    /**
     * DELETE by id
     * @throws JSONException
     */
    @Test
    public void createCar_then_findById_then_deleteById_andValidateIsNotExistent() throws JSONException {
        String licensePlate = "TEST-PLATE-N";
        int seatCount = 5;
        boolean convertible = true;
        int rating = 9;
        String engineType = "HYBRID";

        // CREATE car
        int carId =
                createCarByAPI(licensePlate, seatCount, convertible, rating, engineType)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        assertTrue(carId>0);


        //find by id and validate fields
        findCarByIdAndValidate(carId, licensePlate, seatCount,
                convertible, rating, engineType);

        //delete car
        deleteResource(CARS_URL + carId);

        //find car and expect not found
        delete(CARS_URL + carId)
                .then()
                .statusCode(404);
    }

    /**
     * Find a car and validate response fields
     * @param id The car's d
     * @return
     */
    private ValidatableResponse findCarByIdAndValidate(int id,
                                                       String licensePlate,
                                                       Integer seatCount,
                                                       Boolean convertible,
                                                       Integer rating,
                                                       String engineType) {
        return findCarById(id)
                .assertThat()
                .body("id", equalTo(id))
                .body("licensePlate", equalTo(licensePlate))
                .body("seatCount", equalTo(seatCount))
                .body("convertible", equalTo(convertible))
                .body("rating", equalTo(rating))
                .body("engineType", equalTo(engineType));
    }

    /**
     * Find a car via REST API
     * @param id The car's id
     * @return The HTTP response
     */
    private ValidatableResponse findCarById(int id) {
        return findResource(CARS_URL + "/" + id);
    }
}
