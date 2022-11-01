package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HeaderContainsCondition implements Condition {
    private String expectedHeaderName; // Example: Content-Type = "content/json"

    @Override
    public void check(ValidatableResponse response) {
        response.extract().headers().hasHeaderWithName(expectedHeaderName);
    }


    @Override
    public String toString() {
        return "Should have Header \"" + expectedHeaderName + "\"";
    }
}
