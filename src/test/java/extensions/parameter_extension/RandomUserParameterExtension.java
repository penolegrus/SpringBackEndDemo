package extensions.parameter_extension;

import db_models.User;
import dto.UserDTO;
import helpers.Utils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Collections;

public class RandomUserParameterExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RandomUser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return generateUser(parameterContext.getParameter());
    }

    private Object generateUser(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (UserDTO.class.equals(type)) {
            return new UserDTO("test" + Utils.getRandomInt(), "pass" + Utils.getRandomInt(),
                    Collections.singletonList(Utils.generateGameForDb(true)));
        }
        throw new ParameterResolutionException("No random user is generated for " + type);
    }
}
