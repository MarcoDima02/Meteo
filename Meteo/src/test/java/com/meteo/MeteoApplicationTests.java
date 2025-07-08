package com.meteo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "weather.api.base-url=https://api.open-meteo.com/v1/forecast",
    "weather.geocoding.base-url=https://geocoding-api.open-meteo.com/v1/search",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MeteoApplicationTests {

    @Test
    void contextLoads() {
        // Test che verifica che il contesto Spring si carichi correttamente
    }

}
