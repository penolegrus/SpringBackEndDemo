package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.Argument;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class BodyMatcherListArgsCondition implements Condition {

    private List<Argument> arguments;
    private Matcher matcher;

    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(arguments, matcher);
    }


    @Override
    public String toString() {
        return "The arguments: \"" + arguments.toString() + "\" should match the condition: " + matcher;
    }
}
