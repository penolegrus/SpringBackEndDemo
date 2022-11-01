package assections.conditions;


import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;


@AllArgsConstructor
public class BodyMatcherCondition implements Condition {

    private Matcher matcher;

    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(matcher);
    }

    @Override
    public String toString(){
        return "Response body matches: " + matcher.toString();
    }

}
