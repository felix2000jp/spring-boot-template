package dev.felix2000jp.springboottemplate;

import org.springframework.boot.SpringApplication;

public class TestSpringBootTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringBootTemplateApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
