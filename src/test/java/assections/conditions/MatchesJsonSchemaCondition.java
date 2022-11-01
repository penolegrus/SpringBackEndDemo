package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import org.hamcrest.Matcher;
import io.restassured.module.jsv.JsonSchemaValidator;

import java.io.File;


@AllArgsConstructor
public class MatchesJsonSchemaCondition implements Condition {
    private File schema;

    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
    }

    @Override
    public String toString() {
        return "Response schema matches: " + schema.getName();
    }
}
