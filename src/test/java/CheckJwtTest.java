import helpers.Utils;
import io.restassured.http.ContentType;
import jwt.models.JwtRequest;
import models.ChangeUserPass;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapi.BaseApiTest;


import static helpers.Constants.REGISTER_API_URL;
import static io.restassured.RestAssured.given;
import static restapi.utils.TestUsers.DEMO_USER;

public class CheckJwtTest extends BaseApiTest {


    @Test
    public void jwt() {
        auth(DEMO_USER);
        // given().get("/api/user").then().log().all();
        given().auth().oauth2(token).get("/api/user").then().log().all();
        // given().auth().oauth2(token).get("/api/user/games").then().log().all();
        //given().auth().oauth2(token).get("/api/user/games/1").then().log().all();
    }

    @Test
    public void addUsernew() {
        User user = new User("admin3", "admin");
        given().body(user).post("/api/register").then().log().all();
    }

    @Test
    public void updateUserPass() {
        User user = new User("newLogin" + Utils.getRandomInt(), "oldPass");

        User created = restService.post(REGISTER_API_URL, user).as("register_data", User.class);

        auth(created);

        restService.put(token, "/api/user", new ChangeUserPass("newPassEdited"))
                .hasMessage("User password successfully changed");

        User editedUser = getUser();

        Assertions.assertNotNull(editedUser.getPass());
        Assertions.assertNotEquals(created.getPass(), editedUser.getPass());

        deleteUser();
    }

    @Test
    public void updateUserPassNoValue() {
        User user = new User("newLogin" + Utils.getRandomInt(), "oldPass");

        User created = restService.post(REGISTER_API_URL, user).as("register_data", User.class);

        auth(created);

        restService.put(token,"/api/user", new ChangeUserPass())
                .hasMessage("Body has no password parameter");

        deleteUser();
    }

    @Test
    public void updateUserPassBaseUser() {
        auth(new User("admin", "admin"));
        ChangeUserPass newPass = new ChangeUserPass("newPassword");
        restService.put(token, "/api/user", newPass).hasMessage("Cant update base users");
    }

    private void deleteUser() {
        restService.delete(token, "/api/user");
    }

    private User getUser() {
        return restService.get(token, "/api/user").as(User.class);
    }


}
