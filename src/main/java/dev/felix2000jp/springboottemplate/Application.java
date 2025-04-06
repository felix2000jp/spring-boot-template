package dev.felix2000jp.springboottemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.modulith.Modulith;

@Modulith(sharedModules = {"shared"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
