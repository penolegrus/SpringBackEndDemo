package app;

import helpers.Utils;
import models.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repo.UserRepository;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByLogin("admin") == null
                    && userRepository.findByLogin("demo") == null
                    && userRepository.findByLogin("threadqa") == null) {
                User adminUser = new User();
                adminUser.setLogin("admin");
                adminUser.setPass("admin");
                adminUser.setGames(Collections.singletonList(Utils.generateRandomGame()));

                User demoUser = new User();
                demoUser.setLogin("demo");
                demoUser.setPass("demo");
                demoUser.setGames(Collections.singletonList(Utils.generateRandomGame()));

                User threadQAUser = new User();
                threadQAUser.setLogin("threadqa");
                threadQAUser.setPass("threadqa");
                threadQAUser.setGames(Collections.singletonList(Utils.generateRandomGame()));

                userRepository.saveAll(Stream.of(adminUser, demoUser, threadQAUser).collect(Collectors.toList()));
            }

        };
    }
}
