import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParametrTest extends Const {
    private static Response response;

    @ParameterizedTest
    @ValueSource(strings = {"EUR", "GBP", "JPY"})
    public void currency(String currency) {
        response = given().get(Const.URL + Const.URLLIVE + Const.APIACCESSKEY + Const.URLTOCURRENCY + currency);
        System.out.println(response.asString());
        response.then().statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"EUR", "GBP", "JPY"})
    public void currencyHistURL(String currencyE) {
        response = given().get(Const.URL + Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY + Const.URLTOCURRENCY + currencyE);
        System.out.println(response.asString());
        String expected = response.path("date");
        Integer actualMs = response.path("timestamp");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date((long) actualMs * 1000);
        String actual = format.format(date2.getTime());
        Assertions.assertEquals(expected, actual);
        response.then().body("historical", notNullValue());
    }

    @Test
    public void negativeCurrenciesTest() {
        response = given().get(Const.URL + Const.URLHISTORICALENDPOINT + Const.APIACCESSKEY + Const.URLTOCURRENCY + FAKECURRENCY);
        System.out.println(response.asString());
        response.then().body("error.info", equalTo("You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"));
    }

}