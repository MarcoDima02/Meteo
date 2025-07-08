package com.meteo.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.meteo.entity.WeatherData;
import com.meteo.repository.WeatherDataRepository;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private WeatherService weatherService;

    private WeatherData sampleWeatherData;

    @BeforeEach
    void setUp() {
        // Imposta gli URL di test per Open Meteo
        ReflectionTestUtils.setField(weatherService, "baseUrl", "https://api.open-meteo.com/v1/forecast");
        ReflectionTestUtils.setField(weatherService, "geocodingUrl", "https://geocoding-api.open-meteo.com/v1/search");

        // Crea dati di esempio
        sampleWeatherData = new WeatherData(
            "Roma", "Italia", 20.5, 22.1, 
            "Sereno", "01d", 65, 5.2, 1013, 
            LocalDateTime.now()
        );
    }

    @Test
    void testGetWeatherHistory() {
        // Given
        String cityName = "Roma";
        int days = 7;
        List<WeatherData> mockData = Arrays.asList(sampleWeatherData);
        
        when(weatherDataRepository.findByCityNameAndTimestampBetweenOrderByTimestampDesc(
            anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(mockData);

        // When
        var result = weatherService.getWeatherHistory(cityName, days);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cityName, result.get(0).getCityName());
        assertEquals(20.5, result.get(0).getTemperature());
    }

    @Test
    void testGetCurrentWeatherFromDatabase() {
        // Given
        String cityName = "Roma";
        WeatherData recentData = new WeatherData(
            cityName, "Italia", 25.0, 26.5,
            "Soleggiato", "01d", 60, 4.5, 1015,
            LocalDateTime.now().minusMinutes(5) // Dati recenti
        );
        
        when(weatherDataRepository.findFirstByCityNameOrderByTimestampDesc(cityName))
            .thenReturn(Optional.of(recentData));

        // When
        var result = weatherService.getCurrentWeather(cityName);

        // Then
        assertNotNull(result);
        assertEquals(cityName, result.getCityName());
        assertEquals(25.0, result.getTemperature());
        
        // Verifica che non sia stata chiamata l'API esterna
        verify(weatherDataRepository, never()).save(any(WeatherData.class));
    }

    @Test
    void testGetCurrentWeatherNotFound() {
        // Given
        String cityName = "CittaInesistente";
        when(weatherDataRepository.findFirstByCityNameOrderByTimestampDesc(cityName))
            .thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getCurrentWeather(cityName);
        });
        
        assertTrue(exception.getMessage().contains("Nessun dato meteo disponibile"));
    }

    @Test
    void testUpdateWeatherDataScheduled() {
        // Given
        List<WeatherData> mockLatestData = Arrays.asList(sampleWeatherData);
        when(weatherDataRepository.findLatestWeatherForAllCities())
            .thenReturn(mockLatestData);

        // When
        weatherService.updateWeatherDataScheduled();

        // Then
        verify(weatherDataRepository, atLeastOnce()).deleteByTimestampBefore(any(LocalDateTime.class));
    }
}
