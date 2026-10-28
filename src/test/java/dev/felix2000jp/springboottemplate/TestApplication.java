package dev.felix2000jp.springboottemplate;

import dev.felix2000jp.springboottemplate.system.configurations.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestApplication {

    static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
