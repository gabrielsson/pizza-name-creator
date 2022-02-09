package com.gabrielsson;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PizzaNameResourceTest {

    @Test
    public void postIngredientsYieldsName() {

        given()
                .body(java.util.Arrays.asList(Arrays.asList("a"), Arrays.asList("b")))
                .contentType(ContentType.JSON)
                .when()
                .post("/name")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size", is(2));
    }

    @Test
    public void postSameIngredientsYieldsSameNames() {
        String[] names = given()
                .body(Arrays.asList(Arrays.asList("a", "c"), Arrays.asList("a", "c")))
                .contentType(ContentType.JSON)
                .when()
                .post("/name")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size", is(2))
                .extract().body().as(String[].class);

        Assertions.assertEquals(names[0], names[1]);
    }
}