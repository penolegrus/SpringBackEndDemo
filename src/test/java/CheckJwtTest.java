import io.restassured.http.ContentType;
import jwt.models.JwtRequest;
import jwt.models.JwtResponse;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapi.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CheckJwtTest extends BaseApiTest {

    private String token;

    public void auth(String login, String pass){
        JwtRequest jwtRequest = new JwtRequest(login, pass);

        String jwtResponse = given()
                .contentType(ContentType.JSON)
                .body(jwtRequest)
                .post("/api/login")
                .then().log().all().extract().body().jsonPath().get("token");

        Assertions.assertNotNull(jwtResponse);
        token = jwtResponse;
    }

    //авторизуюсь под админом, получаю токен и я могу лазить по апишке во всем проекте с этим токеном
    @Test
    public void jwt(){
        auth("demo", "password");
        given().auth().oauth2(token).get("/api/user").then().log().all();
        given().auth().oauth2(token).get("/api/user/games").then().log().all();
        given().auth().oauth2(token).get("/api/user/games/1").then().log().all();
    }

    @Test
    public void addUsernew(){
        User user = new User("admin3", "admin");
        given().body(user).post("/users").then().log().all();
    }

}
