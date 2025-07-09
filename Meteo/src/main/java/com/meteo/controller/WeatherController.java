package com.meteo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meteo.dto.WeatherResponse;
import com.meteo.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    
    @Autowired
    private WeatherService weatherService;
    
    /**
     * Endpoint per ottenere il meteo corrente di una città specifica
     */
    @GetMapping("/current/{cityName}")
    public ResponseEntity<WeatherResponse> getCurrentWeather(@PathVariable String cityName) {
        try {
            logger.info("Richiesta meteo per città: {}", cityName);
            WeatherResponse weather = weatherService.getCurrentWeather(cityName);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            logger.error("Errore nel recupero meteo per {}: {}", cityName, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint per ottenere il meteo di tutte le principali città italiane
     */
    @GetMapping("/italy/all")
    public ResponseEntity<List<WeatherResponse>> getAllItalianCitiesWeather() {
        try {
            logger.info("Richiesta meteo per tutte le città italiane");
            List<WeatherResponse> weatherList = weatherService.getAllItalianCitiesWeather();
            return ResponseEntity.ok(weatherList);
        } catch (Exception e) {
            logger.error("Errore nel recupero meteo per le città italiane: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint per ottenere lo storico meteo di una città
     */
    @GetMapping("/history/{cityName}")
    public ResponseEntity<List<WeatherResponse>> getWeatherHistory(
            @PathVariable String cityName,
            @RequestParam(defaultValue = "7") int days) {
        try {
            logger.info("Richiesta storico meteo per città: {} - ultimi {} giorni", cityName, days);
            List<WeatherResponse> history = weatherService.getWeatherHistory(cityName, days);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.error("Errore nel recupero storico per {}: {}", cityName, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint per forzare l'aggiornamento dei dati meteo
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshWeatherData() {
        try {
            logger.info("Aggiornamento manuale dati meteo richiesto");
            weatherService.updateWeatherDataScheduled();
            return ResponseEntity.ok("Dati meteo aggiornati con successo");
        } catch (Exception e) {
            logger.error("Errore nell'aggiornamento dati meteo: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Errore nell'aggiornamento: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint per popolare il database con dati storici (GET version)
     */
    @GetMapping("/populate-historical")
    public ResponseEntity<String> populateHistoricalDataGet(@RequestParam(defaultValue = "7") int days) {
        return populateHistoricalData(days);
    }

    /**
     * Endpoint per popolare il database con dati storici
     */
    @PostMapping("/populate-historical")
    public ResponseEntity<String> populateHistoricalData(@RequestParam(defaultValue = "7") int days) {
        try {
            logger.info("Richiesto popolamento dati storici per {} giorni", days);
            
            if (days > 30) {
                return ResponseEntity.badRequest().body("Massimo 30 giorni supportati");
            }
            
            // Esegui in un thread separato per non bloccare la risposta HTTP
            new Thread(() -> weatherService.populateHistoricalData(days)).start();
            
            return ResponseEntity.ok(String.format("Avviato popolamento dati storici per %d giorni. Controlla i log per il progresso.", days));
        } catch (Exception e) {
            logger.error("Errore nel popolamento dati storici: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Errore: " + e.getMessage());
        }
    }
}
