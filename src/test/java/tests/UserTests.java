package tests;

import assections.Conditions;
import core.TestBase;
import models.user.ChangeUserPass;
import models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static assections.Conditions.*;
import static testdata.TestData.ADMIN_USER;


public class UserTests extends TestBase {

    @Test
    public void testChangPassPositive() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);

        userService.updatePassword("newPassEdited")
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("User password successfully changed"));

        userService.getUserInfo()
                .shouldHave(statusCode(200))
                .shouldHave(keyNotEqualTo("pass", randomTestUser.getPass()));

        userService.deleteAuthedUser();
    }

    @Test
    public void updateUserPassNoValueNegative() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);
        userService.updatePassword(null)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Body has no password parameter"));
        userService.deleteAuthedUser();
    }

    @Test
    public void updateUserPassBaseUserNegative() {
        userService.login(new User("admin", "admin"));
        userService.updatePassword("newPassword")
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cant update base users"));
    }

    @Test
    public void registerSuccessTest() {
        userService.register(randomTestUser)
                .shouldHave(statusCode(201))
                .shouldHave(Conditions.hasSchema(new File("src/test/resources/jsonSchemas/successUserRegisterSchema.json")))
                .shouldHave(hasMessage("User created"));
        userService.login(randomTestUser);
        userService.deleteAuthedUser();
    }

    @Test
    public void negativeRegisterNewUserWithAlreadyLoginExistTest() {
        userService.register(ADMIN_USER)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Login already exist"));
    }


    @Test
    public void negativeRegisterNewUserWithoutPassTest() {
        randomTestUser.setPass(null);
        userService.register(randomTestUser)
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Missing login or password"));

    }

    @Test
    public void deleteUserTestSuccess() {
        userService.register(randomTestUser);
        userService.login(randomTestUser);
        userService.deleteAuthedUser()
                .shouldHave(statusCode(200))
                .shouldHave(hasMessage("User successfully deleted"));
    }

    @Test
    public void deleteUserTestBaseUser() {
        userService.login(ADMIN_USER);
        userService.deleteAuthedUser()
                .shouldHave(statusCode(400))
                .shouldHave(hasMessage("Cant delete base users"));
    }

    @Test
    public void validAuthTest() {
        userService.login(ADMIN_USER)
                .shouldHave(statusCode(200))
                .shouldHave(Conditions.bodyKeyNotNull("token"));
    }

    @Test
    public void invalidAuthTest() {
        userService.login(randomTestUser)
                .shouldHave(statusCode(401));
    }

    @Test
    public void allUsersTest() {
        String[] usersNames = userService.getAllUsers()
                .shouldHave(statusCode(200))
                .as("userLogins", String[].class);
        Assertions.assertTrue(usersNames.length >= 3);
    }
}
