package services;


import assections.AssertableResponse;
import io.restassured.response.ValidatableResponse;
import jwt.models.JwtRequest;
import lombok.extern.slf4j.Slf4j;
import models.user.ChangeUserPass;
import models.user.User;

@Slf4j
public class UserService extends WebService {
    public UserService(String basePath) {
        super(basePath);
    }

    public AssertableResponse register(User user) {
        log.info("register user {}", user);
        return new AssertableResponse(requestSpec.body(user).post("register").then());
    }

    public AssertableResponse getUserInfo() {
        log.info("get user info");
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).get("user").then());
    }

    public AssertableResponse updatePassword(ChangeUserPass password) {
        log.info("update user's password {}", password);
        return new AssertableResponse(requestSpec.auth().oauth2(jwt).body(password).put("user").then());
    }

    public AssertableResponse login(User user) {
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
