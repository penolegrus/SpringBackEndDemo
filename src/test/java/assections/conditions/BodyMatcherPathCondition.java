package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;


@AllArgsConstructor
public class BodyMatcherPathCondition implements Condition {

    private String path;
    private Matcher matcher;


    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(path, matcher);
    }


    @Override
    public String toString() {
        return "Response body field \'" + path +"\' should match the condition: " + matcher;
    }
}
