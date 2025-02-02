package org.example.stavkova_kancelaria_web;

import org.springframework.boot.SpringApplication;

public class TestStavkovaKancelariaWebApplication {

    public static void main(String[] args) {
        SpringApplication.from(StavkovaKancelariaWebApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
