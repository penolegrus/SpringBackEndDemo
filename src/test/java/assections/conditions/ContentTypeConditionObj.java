package assections.conditions;

import assections.Condition;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentTypeConditionObj implements Condition {

    private ContentType contentType;


    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().contentType(contentType);
    }

    @Override
    public String toString(){
        return "Content Type " + contentType.name();
    }

}
