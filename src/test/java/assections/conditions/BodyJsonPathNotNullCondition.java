package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;

@AllArgsConstructor
public class BodyJsonPathNotNullCondition implements Condition {
    private String jsonPath;

    @Override
    public void check(ValidatableResponse response) {
        Assertions.assertNotNull(response.extract().body().jsonPath().get(jsonPath));
    }

    @Override
    public String toString(){
        return "Response body key is not null : " + jsonPath;
    }
}
