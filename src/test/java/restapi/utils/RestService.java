package restapi.utils;

import io.restassured.response.Response;
import models.Message;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RestService {

    private Response response;

    public RestService hasMessage(String messageValue) {
        Message message = response.then().extract().body().jsonPath().getObject("info", Message.class);
        Assertions.assertNotNull(message, "No message in response");
        Assertions.assertEquals(messageValue, message.getMessage());
        return this;
    }

    public RestService hasStatusCode(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode());
        return this;
    }

    public RestService put(String endPoint, Object body) {
        asResponse(given().body(body).put(endPoint));
        return this;
    }

    public RestService put(String jwt, String endPoint, Object body) {
        asResponse(given().auth().oauth2(jwt).body(body).put(endPoint));
        return this;
    }

    public RestService put(String endPoint) {
        asResponse(given().put(endPoint));
        return this;
    }

    public RestService post(String endPoint, Object body) {
        asResponse(given().body(body).post(endPoint));
        return this;
    }

    public RestService post(Object body) {
        asResponse(given().body(body).post());
        return this;
    }

    public RestService delete(String endPoint) {
        asResponse(given().delete(endPoint));
        return this;
    }

    public RestService delete(String jwt, String endPoint) {
        asResponse(given().auth().oauth2(jwt).delete(endPoint));
        return this;
    }

    public RestService delete(String endPoint, Object body) {
        asResponse(given().body(body).delete(endPoint));
        return this;
    }

    public RestService get(String endPoint) {
        asResponse(given().get(endPoint));
        return this;
    }

    public RestService get(String jwt, String endPoint) {
        asResponse(given().auth().oauth2(jwt).get(endPoint));
        return this;
    }

    public RestService get() {
        asResponse(given().get());
        return this;
    }

    private RestService asResponse(Response responseForExtract) {
        response = responseForExtract.then().log().all().extract().response();
        return this;
    }


    public <T> T as(Class<T> clazz) {
        return response.as(clazz);
    }


    public <T> List<T> asList(Class<T> clazz) {
        return response.body().jsonPath().getList("",clazz);
    }

    public <T> List<T> asList(String path,Class<T> clazz) {
        return response.body().jsonPath().getList(path,clazz);
    }

    public Response asResponse() {
        return response;
    }

    public <T> T as(String jsonPath, Class<T> clazz) {
        return response.body().jsonPath().getObject(jsonPath, clazz);
    }

    public <T> T as(String jsonPath) {
        return response.body().jsonPath().getJsonObject(jsonPath);
    }
}
