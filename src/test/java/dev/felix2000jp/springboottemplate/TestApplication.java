package dev.felix2000jp.springboottemplate;

import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(TestcontainersConfiguration.class)
                .withAdditionalProfiles("testcontainers")
                .run(args);
    }

}
