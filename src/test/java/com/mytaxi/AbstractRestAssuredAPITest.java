package com.mytaxi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;
import org.junit.Before;

import static io.restassured.RestAssured.*;

public class AbstractRestAssuredAPITest {
    protected static final String DRIVERS_URL = "drivers";
    protected static final String CARS_URL = "cars";

    @Before
    public void setBaseUri () {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1";
    }

    /**
     * Send the HTTP request via POST method
     * @param path The HTTP request path
     * @param newResource The resource to be created
     * @return The HTTP response
     */
    protected ValidatableResponse createResource(String path, JSONObject newResource) {
        return given()
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
    protected ValidatableResponse findResource(String path) {
        return get(path)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    /**
     * Send the HTTP request via DELETE method
     * @param path The HTTP request path
     * @return A valid response
     */
    protected ValidatableResponse deleteResource(String path) {
        return delete(path)
                .then()
                .statusCode(404);
    }

    /**
     * Send the HTTP request via PUT method
     * @param path The HTTP request path
     * @return A valid response
     */
    protected ValidatableResponse updateResource(String path, JSONObject newResource) {
        return given()
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
    protected ValidatableResponse patchResource(String path, JSONObject newResource) {
        return given()
                 .contentType(ContentType.JSON)
                 .body(newResource.toString())
                 .when()
               .patch(path)
                 .then()
                 .statusCode(204);
    }
}
