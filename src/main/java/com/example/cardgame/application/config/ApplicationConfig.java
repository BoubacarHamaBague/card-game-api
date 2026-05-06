package com.example.cardgame.application.config;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public Random random() {
        return new Random();
    }
}
