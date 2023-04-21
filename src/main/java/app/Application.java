package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"app", "jwt", "service", "new_controllers"} )
@EnableJpaRepositories("repo")
@EntityScan("db_models")
//@ComponentScan(basePackages = {"controllers","app", "jwt", "repository", "service", "models", "db_models", "dto", "new_controllers", "repo"} )
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
