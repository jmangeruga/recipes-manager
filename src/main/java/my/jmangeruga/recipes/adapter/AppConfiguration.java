package my.jmangeruga.recipes.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class AppConfiguration {

    @Bean
    Clock provideClock() {
        return Clock.systemUTC();
    }

}
