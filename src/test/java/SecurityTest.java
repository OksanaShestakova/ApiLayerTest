import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class SecurityTest {
    private static Response response;


    @Test
    public void getResponseCodeTest() {
        response = given().get(Const.URL+ Const.URLLIVE + Const.APIACCESSKEY);
        System.out.println(response.asString());
        response.then().statusCode(200);
    }

    @Test
    public void getSuccessTest() {
        response = given().get(Const.URL+ Const.URLLIVE  + Const.APIACCESSKEY);
        boolean responceValue = response.getBody().jsonPath().getBoolean("success");
        System.out.println("Value:" + responceValue);
    }

    @Test
    public void unauthorizedUserTest() {
        response = given().get(Const.URL + Const.URLLIVE + "APIACCESSKEY");
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo(Const.ERRORMESSAGE));

    }

    @Test
    public void timestampTest() {
        response = given().get(Const.URL+ Const.URLLIVE  + Const.APIACCESSKEY);
        long timestampValue = response.getBody().jsonPath().getLong("timestamp");
        LocalDateTime timestampDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampValue), ZoneId.systemDefault());
        LocalDateTime currentDate = LocalDateTime.now();
        assertTrue("Correct", timestampDate.isBefore(currentDate) || timestampDate.isEqual(currentDate));
        System.out.println("Correct: current "+ currentDate+ " timestamp: "+ timestampDate);
    }
    @Test
    public void sourceTest() {
        response = given().get(Const.URL+ Const.URLLIVE  + Const.APIACCESSKEY);
        response.then().body("source", equalTo("USD"));
    }



    @Test
    public void currenciesTest() {
        response = given().get(Const.URL+ Const.URLLIVE  + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response = given().get(Const.URL + Const.URLLIVE + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR + Const.GBP);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response = given().get(Const.URL + Const.URLLIVE + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR + Const.GBP + Const.JPY);
        System.out.println(response.asString());
        response.then().statusCode(200);
    }
        @Test
        public void negativeCurrenciesTest() {
        response = given().get(Const.URL + Const.URLLIVE+ Const.APIACCESSKEY + Const.URLTOCURRENCY + "ESDT");
        System.out.println(response.asString());
        response.then().body("error.info", equalTo("You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"));
    }
/*HISTORICAL*/
    @Test
    public void historicalResponseCodeTest(){
        response = given().get(Const.URL +Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY);
        System.out.println(response.asString());
        response.then().body("date", notNullValue());
    }
    @Test
    public void negativeNoDateTest(){
        response = given().get(Const.URL+Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY);
        Assertions.assertTrue(response.asString().contains("error") || response.asString().contains("date"));
    }
    @Test
    public void optionalCurrencyParTest(){
        response = given().get(Const.URL+Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response = given().get(Const.URL+Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR + Const.GBP);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response = given().get(Const.URL+Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY + Const.URLTOCURRENCY + Const.EUR + Const.GBP + Const.JPY);
        System.out.println(response.asString());
        response.then().statusCode(200);
    }
}