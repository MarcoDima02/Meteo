package com.meteo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meteo.entity.WeatherData;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    
    // Trova l'ultimo dato meteo per una città
    Optional<WeatherData> findFirstByCityNameOrderByTimestampDesc(String cityName);
    
    // Trova tutti i dati meteo per una città in un range di date
    List<WeatherData> findByCityNameAndTimestampBetweenOrderByTimestampDesc(
        String cityName, LocalDateTime start, LocalDateTime end);
    
    // Trova i dati meteo più recenti per tutte le città
    @Query("SELECT w FROM WeatherData w WHERE w.timestamp = " +
           "(SELECT MAX(w2.timestamp) FROM WeatherData w2 WHERE w2.cityName = w.cityName)")
    List<WeatherData> findLatestWeatherForAllCities();
    
    // Trova tutti i dati di una città ordinati per timestamp
    List<WeatherData> findByCityNameOrderByTimestampDesc(String cityName);
    
    // Elimina i dati più vecchi di una certa data
    void deleteByTimestampBefore(LocalDateTime timestamp);
    
    // Conta i record per una città
    long countByCityName(String cityName);
    
    // Trova tutti i dati delle ultime 24 ore
    @Query("SELECT w FROM WeatherData w WHERE w.timestamp >= :since ORDER BY w.timestamp DESC")
    List<WeatherData> findWeatherDataSince(@Param("since") LocalDateTime since);
}
