package restapi;

import models.trains.NumbersPow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class NumsApiTest extends BaseApiTest {

    @Test
    public void apiVersionTest(){
        String version = restService.get("/api/easy/version").as("apiVersion");
        Assertions.assertTrue(version.matches("^(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$"));
        System.out.println(version);
    }

    @Test
    public void check(){
        NumbersPow value = restService.get("/api/easy/nums").as("numbersPow.nums", NumbersPow.class);
        System.out.println(value);
    }

}
