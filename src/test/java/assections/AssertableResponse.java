package assections;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AssertableResponse {

    public final ValidatableResponse response;

    @Step
    public AssertableResponse shouldHave(Condition condition){
        log.info("Check for: " + condition.toString());
        condition.check(response);
        return this;
    }

    @Step
    public AssertableResponse shouldHave(Condition... conditions){
        List<Condition> conds = Arrays.asList(conditions);
        conds.forEach(condition -> {
            log.info("Check for: " + condition.toString());
            condition.check(response);
        });
        return this;
    }

    public <T> T as(Class<T> clazz){
        return response.extract().body().as(clazz);
    }

    public <T> T as(String jsonPath, Class<T> clazz){
        return response.extract().body().jsonPath().getObject(jsonPath,clazz);
    }

    public <T> List<T> asList(Class<T> clazz) {
        return response.extract().body().jsonPath().getList("",clazz);
    }

    public <T> List<T> asList(String jsonPath, Class<T> clazz) {
        return response.extract().body().jsonPath().getList(jsonPath,clazz);
    }

    public Response asResponse(){
        return response.extract().response();
    }

    public byte[] asByteArray(){
        return response.extract().asByteArray();
    }
}
