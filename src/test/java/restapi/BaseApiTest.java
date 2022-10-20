package restapi;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import restapi.utils.RestSender;


public class BaseApiTest {

    //атомарность, сетап данных перед каждым тестом, очистка окружения

    protected static final RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
    protected static final RestSender rest = new RestSender();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.requestSpecification = reqBuilder
                .setContentType(ContentType.JSON)
                .build();
    }
}
