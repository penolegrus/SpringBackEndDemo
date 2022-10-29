package app;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import jwt.models.JwtResponse;
import models.InfoMessage;
import models.trains.ApiVersion;
import models.game.RegisterGameResponse;
import models.user.RegisterUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConf {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .additionalModels(
                        typeResolver.resolve(InfoMessage.class),
                        typeResolver.resolve(RegisterUserResponse.class),
                        typeResolver.resolve(RegisterGameResponse.class),
                        typeResolver.resolve(ApiVersion.class),
                        typeResolver.resolve(JwtResponse.class)
                )
                .select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("QA Automation Train App for Rest Assured")
                .version("1.0.2")
                .contact(new Contact().url("https://threadqa.ru").name("ThreadQA")));
    }

    //http://localhost:8080/swagger-ui/index.html
    //http://localhost:8080/v2/api-docs
}
