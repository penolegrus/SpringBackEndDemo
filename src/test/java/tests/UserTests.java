package tests;

import assections.Conditions;
import core.TestBase;
import extensions.listeners.AllureLoggingListener;
import extensions.parameter_extension.RandomUser;
import extensions.parameter_extension.RandomUserParameterExtension;
import models.user.ChangeUserPass;
import models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import static testdata.TestData.ADMIN_USER;


@ExtendWith({AllureLoggingListener.class, RandomUserParameterExtension.class})
public class UserTests extends TestBase {

    @Test
    public void testChangPassPositive(@RandomUser User user) {
        userService.register(user);

        userService.login(user);

        userService.updatePassword(new ChangeUserPass("newPassEdited"))
                .shouldHave(Conditions.hasMessage("User password successfully changed"));

        User updatedUser = userService.getUserInfo().as(User.class);

        Assertions.assertNotNull(updatedUser.getPass());
        Assertions.assertNotEquals(user.getPass(), updatedUser.getPass());
        userService.deleteAuthedUser();
    }

    @Test
    public void updateUserPassNoValueNegative(@RandomUser User user) {
        userService.register(user);
        userService.login(user);
        userService.updatePassword(new ChangeUserPass())
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Body has no password parameter"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateUserPassBaseUserNegative() {
        userService.login(new User("admin", "admin"));
        userService.updatePassword(new ChangeUserPass("newPassword"))
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cant update base users"));
    }

    @Test
    public void registerSuccessTest(@RandomUser User user) {
        userService.register(user)
                .shouldHave(Conditions.statusCode(201))
                .shouldHave(Conditions.hasSchema(new File("src/test/resources/jsonSchemas/successUserRegisterSchema.json")))
                .shouldHave(Conditions.hasMessage("User created"));
        userService.login(user);
        userService.deleteAuthedUser();
    }

    @Test
    public void negativeRegisterNewUserWithAlreadyLoginExistTest() {
        User user = new User("admin", "admin");
        userService.register(user)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Login already exist"));
    }


    @Test
    public void negativeRegisterNewUserWithoutPassTest(@RandomUser User user) {
        user.setPass(null);
        userService.register(user)
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Missing login or password"));

    }

    @Test
    public void deleteUserTestSuccess(@RandomUser User user) {
        userService.register(user);
        userService.login(user);
        userService.deleteAuthedUser()
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.hasMessage("User successfully deleted"));
    }

    @Test
    public void deleteUserTestBaseUser() {
        userService.login(ADMIN_USER);
        userService.deleteAuthedUser()
                .shouldHave(Conditions.statusCode(400))
                .shouldHave(Conditions.hasMessage("Cant delete base users"));
    }

    @Test
    public void validAuthTest() {
        userService.login(ADMIN_USER)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyKeyNotNull("token"));
    }

    @Test
    public void invalidAuthTest(@RandomUser User user) {
        userService.login(user)
                .shouldHave(Conditions.statusCode(401));
    }

    @Test
    public void allUsersTest(){
        String[] usersNames = userService.getAllUsers()
                .shouldHave(Conditions.statusCode(200))
                .as("userLogins", String[].class);
        Assertions.assertTrue(usersNames.length>=3);
    }
}
