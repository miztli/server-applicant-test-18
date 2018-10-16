package com.mytaxi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;

import static io.restassured.RestAssured.*;

/**
 * Abstract REST API Test class.
 * Provides default CRUD method calls and validation.
 */
public class AbstractRestAssuredAPITest {
    protected static final String DRIVERS_URL = "drivers";
    protected static final String SEARCH_URL = "search";
    protected static final String CARS_URL = "cars";
    protected static final String LOGIN = "login";
    protected static final String USER = "miztli";
    protected static final String PASSWORD = "password";
    protected static final String HEADER_AUTHORIZATION = "Authorization";
    protected static final String FIELD_ID = "id";
    protected static final String FIELD_ONLINE_STATUS = "onlineStatus";
    protected static final String FIELD_LICENSE_PLATE = "licensePlate";
    protected static final String FIELD_SEAT_COUNT = "seatCount";
    protected static final String FIELD_CONVERTIBLE = "convertible";
    protected static final String FIELD_MANUFACTURER_NAME = "manufacturerName";
    protected static final String FIELD_RATING = "rating";
    protected static final String FIELD_ENGINE_TYPE = "engineType";
    protected static final String FIELD_USERNAME = "username";
    protected static final String FIELD_NAME = "name";
    protected static final String FIELD_PASSWORD = "password";
    protected static final String FIELD_MANUFACTURER_DTO = "manufacturerDTO";
    protected String token;

    /**
     * Set defaults : replace for application-test.properties
     */
    @Before
    public void setBaseUri () {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1";
    }

    /**
     * Authenticate againts REST API.
     * Synchronize to avoid JUnit threads access at the same time.
     * @return The Bearer Token
     * @throws JSONException if the provided user cannot be parsed to JSON
     */
    protected synchronized String authenticated() throws JSONException {
        JSONObject principal = new JSONObject()
                                        .put(FIELD_USERNAME, USER)
                                        .put(FIELD_PASSWORD, PASSWORD);
        if (token == null){ //Avoid authentication in each call
            token =
                given()
                    .basePath("/")
                    .contentType(ContentType.JSON)
                    .body(principal.toString())
                    .when()
                    .post(LOGIN)
                    .then()
                    .statusCode(200)
                        .extract()
                        .header(HEADER_AUTHORIZATION);

        }
        return token;
    }

    /**
     * Send the HTTP request via POST method
     * @param path The HTTP request path
     * @param newResource The resource to be created
     * @return The HTTP response
     */
    protected ValidatableResponse createResource(String path, JSONObject newResource) throws JSONException {
        return given()
                 .header(HEADER_AUTHORIZATION, authenticated())
                 .contentType(ContentType.JSON)
                 .body(newResource.toString())
                 .when()
                 .post(path)
               .then()
                 .contentType(ContentType.JSON)
                 .statusCode(201);
    }

    /**
     * Send the HTTP request via GET method
     * @param path The HTTP request path
     * @return A valid JSON response
     */
    protected ValidatableResponse findResource(String path) throws JSONException {
        return given()
                 .header(HEADER_AUTHORIZATION, authenticated())
               .get(path)
                 .then()
                 .statusCode(200)
                 .contentType(ContentType.JSON);
    }

    /**
     * Send the HTTP request via DELETE method
     * @param path The HTTP request path
     * @return A valid response
     */
    protected ValidatableResponse deleteResource(String path) throws JSONException {
        return given()
                 .header(HEADER_AUTHORIZATION, authenticated())
               .delete(path)
                 .then()
                 .statusCode(204);
    }

    /**
     * Send the HTTP request via PUT method
     * @param path The HTTP request path
     * @return A valid response
     */
    protected ValidatableResponse updateResource(String path, JSONObject newResource) throws JSONException {
        return given()
                 .header(HEADER_AUTHORIZATION, authenticated())
                 .contentType(ContentType.JSON)
                 .body(newResource.toString())
                 .when()
               .put(path)
                 .then()
                 .statusCode(204);
    }

    /**
     * Send the HTTP request via PUT method
     * @param path The HTTP request path
     * @return A valid response
     */
    protected ValidatableResponse patchResource(String path, JSONObject newResource) throws JSONException {
        return given()
                 .header(HEADER_AUTHORIZATION, authenticated())
                 .contentType(ContentType.JSON)
                 .body(newResource.toString())
                 .when()
               .patch(path)
                 .then()
                 .statusCode(204);
    }
}
