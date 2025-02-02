package org.example.stavkova_kancelaria_web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class StavkovaKancelariaWebApplicationTests {

    @Test
    void contextLoads() {
    }

}
