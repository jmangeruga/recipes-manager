package my.jmangeruga.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RecipesApp {

    public static void main(String[] args) {
        SpringApplication.run(RecipesApp.class, args);
    }

}
