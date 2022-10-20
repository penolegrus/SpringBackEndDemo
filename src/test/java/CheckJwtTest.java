import io.restassured.http.ContentType;
import jwt.models.JwtRequest;
import jwt.models.JwtResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapi.BaseApiTest;

import static io.restassured.RestAssured.given;

public class CheckJwtTest extends BaseApiTest {

    private String token;

    public void auth(String login, String pass){
        JwtRequest jwtRequest = new JwtRequest(login, pass);

        String jwtResponse = given()
                .contentType(ContentType.JSON)
                .body(jwtRequest)
                .post("/api/authenticate")
                .then().log().all().extract().body().jsonPath().get("token");

        Assertions.assertNotNull(jwtResponse);
        token = jwtResponse;
    }

    //авторизуюсь под админом, получаю токен и я могу лазить по апишке во всем проекте с этим токеном
    @Test
    public void jwt(){
        auth("admin", "admin");
        given().auth().oauth2(token).get("/api/users/2").then().log().all();
    }
}
