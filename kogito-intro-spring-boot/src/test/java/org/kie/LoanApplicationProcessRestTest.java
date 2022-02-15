package org.kie;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KogitoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LoanApplicationProcessRestTest {

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void setup() {
        RestAssured.port = randomServerPort;
    }

    @Test
    public void testLoanApplicationApprovedRest() {
        
        String payload = "{\"amount\" : 4999}";
    
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload).when()
                .post("/LoanApplication")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("amount", CoreMatchers.equalTo(4999))
                .body("approved", CoreMatchers.equalTo(true));
    }

    @Test
    public void testLoanApplicationDeclinedRest() {
        
        String payload = "{\"amount\" : 5000}";

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload).when()
                .post("/LoanApplication")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("amount", CoreMatchers.equalTo(5000))
                .body("approved", CoreMatchers.equalTo(false)); 
    }
    
}
