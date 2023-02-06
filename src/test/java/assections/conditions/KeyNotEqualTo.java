package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@AllArgsConstructor
public class KeyNotEqualTo implements Condition {
    private Object value;
    private String key;
    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(key, not(equalTo(value)));
    }
}
