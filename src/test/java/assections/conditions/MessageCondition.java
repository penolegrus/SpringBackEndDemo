package assections.conditions;

import assections.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import models.messages.Message;
import org.junit.jupiter.api.Assertions;

@AllArgsConstructor
public class MessageCondition implements Condition {
    private String messageValue;

    @Override
    public void check(ValidatableResponse response) {
        Message message = response.extract().body().jsonPath().getObject("info", Message.class);
        Assertions.assertEquals(message.getMessage(), messageValue);
    }

    @Override
    public String toString(){
        return "Message matches: " + messageValue;
    }
}
