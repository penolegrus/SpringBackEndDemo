package restapi;

import models.NumbersPow;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class NumsApiTest extends BaseApiTest {

    @Test
    public void check(){
        NumbersPow value = restService.get("/api/nums").as("numbersPow.nums", NumbersPow.class);
        System.out.println(value);
    }

}
