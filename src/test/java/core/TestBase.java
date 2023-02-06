package core;

import extensions.listeners.AllureLoggingListener;
import extensions.parameter_extension.RandomUser;
import extensions.parameter_extension.RandomUserParameterExtension;
import io.restassured.RestAssured;
import models.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import services.UserService;

@ExtendWith({AllureLoggingListener.class, RandomUserParameterExtension.class})
public class TestBase {

    protected User randomTestUser;

    protected static UserService userService;

    @BeforeAll
    static void setUpTests() {
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        userService = new UserService();
    }
    @BeforeEach
    public void generateUser(@RandomUser User user){
        randomTestUser = user;
    }

    @AfterEach
    public void reset() {
        userService.resetSpec();
    }
}
