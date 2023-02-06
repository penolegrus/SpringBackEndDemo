package assections;

import assections.conditions.*;
import io.restassured.http.ContentType;
import io.restassured.specification.Argument;
import org.hamcrest.Matcher;

import java.io.File;
import java.util.List;

public class Conditions {
    public static StatusCodeCondition statusCode(int code) {
        return new StatusCodeCondition(code);
    }

    public static ContentTypeCondition contentType(String contentType) {
        return new ContentTypeCondition(contentType);
    }

    public static ContentTypeConditionObj contentType(ContentType contentType){
        return new ContentTypeConditionObj(contentType);
    }

    public static BodyMatcherCondition body(Matcher matcher) {
        return new BodyMatcherCondition(matcher);
    }

    public static BodyMatcherPathCondition body(String path, Matcher matcher){
        return new BodyMatcherPathCondition(path, matcher);
    }

    public static BodyMatcherListArgsCondition body(List<Argument> arguments, Matcher matcher) {
        return new BodyMatcherListArgsCondition(arguments, matcher);
    }

    public static BodyMatcherPathArgsCondition body(String path, List<Argument> arguments, Matcher matcher) {
        return new BodyMatcherPathArgsCondition(path, arguments, matcher);
    }

    public static BodyStringCondition bodyContains(String text){
        return new BodyStringCondition(text);
    }

    public static HeaderStringCondition header(String headerName, String expectedValue){
        return new HeaderStringCondition(headerName, expectedValue);
    }

    public static HeaderMatcherCondition header(String headerName, Matcher matcher){
        return new HeaderMatcherCondition(headerName, matcher);
    }

    public static HeaderContainsCondition header(String expectedHeaderName){
        return new HeaderContainsCondition(expectedHeaderName);
    }

    public static BodyJsonPathNotNullCondition bodyKeyNotNull(String jsonPath){
        return new BodyJsonPathNotNullCondition(jsonPath);
    }

    public static MessageCondition hasMessage(String expectedMessage){
        return new MessageCondition(expectedMessage);
    }

    public static MatchesJsonSchemaCondition hasSchema(File schema){
        return new MatchesJsonSchemaCondition(schema);
    }
    public static KeyNotEqualTo keyNotEqualTo(String key, Object value) {
        return new KeyNotEqualTo(value,key);
    }
}
