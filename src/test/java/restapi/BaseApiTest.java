package restapi;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import jwt.models.JwtRequest;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import restapi.utils.RestService;

import static io.restassured.RestAssured.given;


public class BaseApiTest {

    //атомарность, сетап данных перед каждым тестом, очистка окружения

    protected static final RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
    protected static final RestService restService = new RestService();
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.requestSpecification = reqBuilder
                .setContentType(ContentType.JSON)
                .build();
    }


    protected String token;

    public void auth(User user){
        JwtRequest jwtRequest = new JwtRequest(user.getLogin(), user.getPass());

        String jwtResponse = given()
                .contentType(ContentType.JSON)
                .body(jwtRequest)
                .post("/api/login")
                .then().log().all().extract().body().jsonPath().get("token");

        Assertions.assertNotNull(jwtResponse);
        token = jwtResponse;
    }
}
