//package com.mytaxi;
//
//import io.restassured.RestAssured;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import static io.restassured.RestAssured.get;
//import static org.hamcrest.CoreMatchers.equalTo;
//
//@RunWith(SpringJUnit4ClassRunner.class)
////@TestPropertySource(value={"classpath:application.properties"}) use in case we need props
//@SpringBootTest(classes = MytaxiServerApplicantTestApplication.class)
//public class MytaxiServerApplicantTestApplicationTests
//{
//
////    @Test
////    public void contextLoads()
////    {
////    }
//    @Before
//    public void setBaseUri () {
//        RestAssured.port = 8080;
//        RestAssured.baseURI = "http://localhost"; // replace as appropriate
//    }
//
//    @Test
//    public void  findDriverById() {
//        get("/v1/drivers/2")
//            .then()
//            .assertThat()
//            .body("username", equalTo("driver01"));
//    }
//}
