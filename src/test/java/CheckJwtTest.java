import helpers.Utils;
import models.user.ChangeUserPass;
import models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import restapi.BaseApiTest;


import static helpers.Constants.REGISTER_API_URL;
import static io.restassured.RestAssured.given;

public class CheckJwtTest extends BaseApiTest {

    @Test
    public void addUsernew() {
        auth(new User("123","3"));
//        User user = new User("admin3", "admin");
//        given().body(user).post("/api/register").then().log().all();
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
