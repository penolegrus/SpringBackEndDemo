package core;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import services.FileService;
import services.GameService;
import services.JsonService;
import services.UserService;

public class TestBase {
    public static UserService userService;
    public static FileService fileService;
    public static JsonService jsonService;
    public static GameService gameService;

    @BeforeAll
    static void setUpTests(){
        RestAssured.baseURI = "http://localhost:8080/api";
        userService = new UserService("/");
        fileService = new FileService("/files/");
        jsonService = new JsonService("/easy/");
        gameService = new GameService("/user/");
    }

    @AfterEach
    public void reset(){
        userService.resetSpec();
    }
}
