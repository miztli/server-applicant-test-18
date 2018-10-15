package com.mytaxi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class DriverRestApiIT {
    private static final String DRIVERS_URL = "drivers";

    @Before
    public void setBaseUri () {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1";
//        RestAssured.config =
//                RestAssured.config()
//                           .encoderConfig(
//                                EncoderConfig
//                                    .encoderConfig()
//                                    .encodeContentTypeAs(DEFAULT_CONTENT_TYPE, ContentType.JSON));
    }

    @Test
    public void createValidUser_then_validateFields() throws JSONException {
        createDriverByAPI("test-driver-1000", "test-password-1000")
            .then()
              .contentType(ContentType.JSON)
              .statusCode(201)
              .assertThat()
                 .body("id", notNullValue())
                 .body("username", notNullValue())
                 .body("password", notNullValue());
    }

    @Test
    public void createNewDriver_then_lookForCreatedIdViaAPI() throws JSONException {
        String testUsername = "test-driver-1001";
        String testPassword = "test-password-1001";
        int createdId =
          createDriverByAPI(testUsername, testPassword)
            .then()
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath()
                .getInt("id");

          findResource(DRIVERS_URL + "/" + createdId)
                .assertThat()
                .body("id", equalTo(createdId))
                .body("username", equalTo(testUsername))
                .body("password", equalTo(testPassword));
    }

    @Test
    public void createTwoNewDrivers_then_getDriverListAndValidateFields() throws JSONException {
        String testUsername1 = "test-driver-1111";
        String testPassword1 = "test-password-1111";
        int createdId1 =
                createDriverByAPI(testUsername1, testPassword1)
                        .then()
                        .contentType(ContentType.JSON)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        String testUsername2 = "test-driver-2222";
        String testPassword2 = "test-password-2222";
        int createdId2 =
                createDriverByAPI(testUsername2, testPassword2)
                        .then()
                        .contentType(ContentType.JSON)
                        .extract()
                        .jsonPath()
                        .getInt("id");

        List<Map<String, Object>> driverList = findResource(DRIVERS_URL)
                                    .extract()
                                    .jsonPath()
                                    .getList("");

        driverList.forEach( driver -> {
            System.out.println("#################################################");
            System.out.println("#################################################");
            System.out.println("#################################################");
            System.out.println("#################################################");
            System.out.println("#################################################");
            System.out.println("#################################################");
            System.out.println(driver.toString());
            Integer id = (Integer) driver.get("id");
            String name = (String) driver.get("name");
            String password = (String) driver.get("password");
        });


    }

    private Response createDriverByAPI(String username, String password) throws JSONException {
        JSONObject newDriver = new JSONObject()
                .put("username", username)
                .put("password", password);

        return createResource(DRIVERS_URL, newDriver);
    }

    private Response createResource(String path, JSONObject newResource) {
        return given()
                  .contentType(ContentType.JSON)
                  .body(newResource.toString())
                  .when()
                  .post(path);
    }

    private ValidatableResponse findResource(String path) {
        return get(path)
                .then()
                .contentType(ContentType.JSON);
    }
}
