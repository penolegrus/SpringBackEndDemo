package restapi;

import helpers.Utils;
import io.restassured.path.json.JsonPath;
import models.ChangeUserPass;
import models.Message;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static helpers.Constants.*;


public class UserTests extends BaseApiTest {

    private List<User> getAllUsers() {
        return rest.get(USERS_API_URL).asList(User.class);
    }

    @Test
    public void testAdd10Users() {
        List<User> users = getAllUsers();

        String messageToAssert = "10 users added to database. Users count - " + (users.size() + 10);

        rest.get(USERS_API_URL + "/addUsers/10").hasMessage(messageToAssert);

        List<User> usersAfter = getAllUsers();

        Assertions.assertEquals(usersAfter.size(), users.size() + 10);
    }

    @Test//флаки тест
    public void deleteUsersIfUsersMoreThan200() {
        List<User> users = getAllUsers();

        Message message = rest.get(USERS_API_URL + "/addUsers/210").as("info", Message.class);

        Assertions.assertTrue(message.getMessage().contains("210 users added to database"));
        List<User> users2 = getAllUsers();
        Assertions.assertEquals(users2.size(), users.size());
        Assertions.assertEquals(users2.size(), 99);
    }


    @Test
    public void validAuthTest() {
        JsonPath response = rest.post(LOGIN_API_URL, new User("admin", "admin")).asResponse().jsonPath();
        Assert.assertEquals(response.get("status"), "success");
        Assert.assertNotNull(response.get("jwt"));
    }

    @Test
    public void invalidAuthTest() {
        User user = new User("fake", "fak");
        rest.post(LOGIN_API_URL, user).hasMessage("User not found");
    }

    @Test
    public void deleteUserTestSuccess() {
        User user = new User("fakeValue" + Utils.getRandomInt(), "fakePass");

        User created = rest.post(REGISTER_API_URL, user).as("register_data", User.class);

        rest.delete(USERS_API_URL + "/" + created.getId()).hasMessage("User successfully deleted");
    }

    @Test
    public void deleteUserTestBaseUser() {
        rest.delete(USERS_API_URL + "/1").hasMessage("Cant delete base users");
    }

    @Test
    public void deleteNotExistUserTest() {
        rest.delete(USERS_API_URL + "/-1").hasMessage("User not found to delete");
    }

    @Test
    public void updateUserPass() {
        User user = new User("newLogin" + Utils.getRandomInt(), "oldPass");

        User created = rest.post(REGISTER_API_URL, user).as("register_data", User.class);

        rest.put(USERS_API_URL + "/" + created.getId(), new ChangeUserPass("newPassEdited"))
                .hasMessage("User password successfully changed");

        User editedUser = getUser(created.getId());

        Assertions.assertNotNull(editedUser.getPass());
        Assertions.assertNotEquals(created.getPass(), editedUser.getPass());

        deleteUser(editedUser.getId());
    }

    @Test
    public void updateUserPassNoValue() {
        User user = new User("newLogin" + Utils.getRandomInt(), "oldPass");

        User created = rest.post(REGISTER_API_URL, user).as("register_data", User.class);

        rest.put(USERS_API_URL + "/" + created.getId(), new ChangeUserPass())
                .hasMessage("Body has no password parameter");

        deleteUser(created.getId());
    }

    @Test
    public void updateUserPassBaseUser() {
        ChangeUserPass newPass = new ChangeUserPass("newPassword");
        rest.put(USERS_API_URL + "/1", newPass).hasMessage("Cant update base users");
    }

    private void deleteUser(Integer userId) {
        rest.delete(USERS_API_URL + "/" + userId);
    }

    private User getUser(Integer userId) {
        return rest.get(USERS_API_URL + "/" + userId).as(User.class);
    }

}
