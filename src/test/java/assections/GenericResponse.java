package assections;

import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GenericResponse<T> {

    public final ValidatableResponse response;
    private final TypeRef<T> clazz;

    @Step
    public GenericResponse<T> shouldHave(Condition condition) {
        log.info("Check for: " + condition.toString());
        condition.check(response);
        return this;
    }

    @Step
    public GenericResponse<T> shouldHave(Condition... conditions) {
        List<Condition> conds = Arrays.asList(conditions);
        conds.forEach(condition -> {
            log.info("Check for: " + condition.toString());
            condition.check(response);
        });
        return this;
    }

    public T asObject() {
        return response.extract().body().as(clazz);
    }

    public T asObject(String jsonPath){
        return response.extract().body().jsonPath().getObject(jsonPath,clazz);
    }

    public List<T> asList() {
        return response.extract().body().jsonPath().getList("",clazz.getTypeAsClass());
    }

    public List<T> asList(String jsonPath) {
        return response.extract().body().jsonPath().getList(jsonPath,clazz.getTypeAsClass());
    }

    public Response asResponse() {
        return response.extract().response();
    }

    public byte[] asByteArray() {
        return response.extract().asByteArray();
    }
}
