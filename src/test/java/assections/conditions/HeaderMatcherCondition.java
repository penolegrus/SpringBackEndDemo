package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;

@AllArgsConstructor
public class HeaderMatcherCondition implements Condition {

    private String headerName;
    private Matcher matcher;

    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().header(headerName, matcher);
    }


    @Override
    public String toString() {
        return "Header \"" + headerName + "\" " + matcher.toString();
    }

}
