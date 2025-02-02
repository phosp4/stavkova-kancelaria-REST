package org.example.stavkova_kancelaria_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// todo je to takto spravne?
//@SpringBootApplication
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration.class})
public class StavkovaKancelariaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StavkovaKancelariaWebApplication.class, args);
    }

}
