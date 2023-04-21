package services;


import assections.AssertableResponse;
import db_models.User;
import dto.UserDTO;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jwt.models.JwtRequest;
import lombok.extern.slf4j.Slf4j;
import models.user.ChangeUserPass;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.CompletableFuture.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static sun.nio.cs.Surrogate.is;


@Slf4j
public class UserService extends WebService {
    public UserService() {
        super("/");
    }

    public AssertableResponse register(UserDTO user) {
        log.info("register user {}", user);
        return new AssertableResponse(requestSpec.body(user).post("signup").then());
    }

    public AssertableResponse getUserInfo() {
        log.info("get user info");
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).get("user").then());
    }

    public AssertableResponse updatePassword(String password) {
        log.info("update user's password {}", password);
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(new ChangeUserPass(password)).put("user").then());
    }

    public AssertableResponse login(UserDTO user) {
        JwtRequest jwtRequest = new JwtRequest(user.getLogin(), user.getPass());
        log.info("login user {}", jwtRequest);
        ValidatableResponse response = requestSpec.body(jwtRequest).post("login").then();
        jwt = response.extract().body().jsonPath().get("token");
        return new AssertableResponse(response);
    }

    public AssertableResponse deleteAuthedUser() {
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).delete("user").then());
    }

    public AssertableResponse getAllUsers(){
        return new AssertableResponse(requestSpec.get("users").then());
    }
}
