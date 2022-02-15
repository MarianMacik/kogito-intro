package org.kie;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class LoanApplicationProcessRestTest {

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
