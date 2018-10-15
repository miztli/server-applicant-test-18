package com.mytaxi;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.equalTo;

public class DriverControllerApiIT {
    @Before
    public void setBaseUri () {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost"; // replace as appropriate
    }

    @Test
    public void  findDriverById() {
        get("/v1/drivers/1")
                .then()
                .assertThat()
                .body("username", equalTo("driver01"));
    }
}
