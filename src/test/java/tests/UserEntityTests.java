package tests;

import assections.Conditions;
import core.TestBase;
import helpers.Utils;
import io.restassured.http.ContentType;
import models.game.Game;
import models.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static assections.Conditions.*;
import static io.restassured.RestAssured.given;
import static testdata.TestData.ADMIN_USER;


public class UserEntityTests extends TestBase {

    @Test
    public void addDbTest(){
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            games.add(Utils.generateGameForDb(false));
        }

        User userDTO = new User("demo", "demo", games);
        given().contentType(ContentType.JSON)
                .body(userDTO)
                .post("http://localhost:8080/api/signup")
                .then().log().all();
    }

//    @Test
//    public void updatePassTest(){
//        JwtRequest jwtRequest = new JwtRequest("threadqa", "iloveqa");
//
//        String jwt = given().contentType(ContentType.JSON)
//                .body(jwtRequest)
//                .post("http://localhost:8080/api/login").then().log().all()
//                .extract().body().jsonPath().get("token");
//
//        given().contentType(ContentType.JSON)
//                .auth().oauth2(jwt)
//                .body(new ChangeUserPass("newpass"))
//                .put("http://localhost:8080/api/user")
//                .then().log().all();
//    }

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
        List<String> usersNames = userService.getAllUsers()
                .shouldHave(statusCode(200))
                .asList(String.class);
        Assertions.assertTrue(usersNames.size() >= 3);
    }
}
