package assections.conditions;


import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContentTypeCondition implements Condition {

    private String contentType;


    @Override
    public void check(ValidatableResponse response) {
        response.assertThat().contentType(contentType);
    }


    @Override
    public String toString(){
        return "Content Type " + contentType;
    }

}
