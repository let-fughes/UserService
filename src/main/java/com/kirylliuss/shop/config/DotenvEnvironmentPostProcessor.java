package com.kirylliuss.shop.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Properties;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            Properties properties = new Properties();
            dotenv.entries().forEach(entry ->
                    properties.setProperty(entry.getKey(), entry.getValue()));

            PropertiesPropertySource propertySource =
                    new PropertiesPropertySource("dotenv", properties);

            environment.getPropertySources().addFirst(propertySource);

            System.out.println(".env variables loaded: " + properties.keySet());

        } catch (Exception e) {
            System.err.println("Failed to load .env file: " + e.getMessage());
        }
    }
}