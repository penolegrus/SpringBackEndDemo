package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;


@AllArgsConstructor
public class BodyStringCondition implements Condition {

    private String bodyText;

    @Override
    public void check(ValidatableResponse response) {
        String body = response.assertThat().extract().body().asString();
        Assertions.assertTrue(body.contains(bodyText), this::toString);
    }

    @Override
    public String toString(){
        return "Body should contains: " + bodyText;
    }
}
