package app;

import com.fasterxml.classmate.TypeResolver;
import dto.SuccessRegisterResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jwt.models.JwtResponse;
import models.InfoMessage;
import models.trains.ApiVersion;
import models.game.RegisterGameResponse;
import models.user.RegisterUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SpringFoxConf {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .additionalModels(
                        typeResolver.resolve(InfoMessage.class),
                        typeResolver.resolve(SuccessRegisterResponse.class),
                        typeResolver.resolve(RegisterGameResponse.class),
                        typeResolver.resolve(ApiVersion.class),
                        typeResolver.resolve(JwtResponse.class)
                )
                .apiInfo(metadata())
             //   .securitySchemes(Collections.singletonList(apiKey()))
             //   .securityContexts(Collections.singletonList(securityContext()))
                .select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()//
                .title("QA Automation Train App for Rest Assured")//
                .description("Тренируйся в отправке http запросов от канала ThreadQA")
                .version("1.0.0")
                .license("ThreadQA").licenseUrl("https://www.youtube.com/@threadqa").build();
    }

//    private ApiKey apiKey() {
//        return new ApiKey("Authorization", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.any())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
//    }

    //http://localhost:8080/swagger-ui/index.html
    //http://localhost:8080/v2/api-docs
}
