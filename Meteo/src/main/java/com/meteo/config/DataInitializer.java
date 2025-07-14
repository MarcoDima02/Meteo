package com.meteo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.meteo.repository.WeatherDataRepository;
import com.meteo.service.WeatherService;

/**
 * Inizializzatore dati che popola il database con dati storici se è vuoto
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("🚀 Inizializzazione dati meteo...");
        
        // Controlla se il database è vuoto
        long dataCount = weatherDataRepository.count();
        logger.info("📊 Dati esistenti nel database: {}", dataCount);
        
        if (dataCount < 50) { // Se abbiamo meno di 50 record, popola con dati storici
            logger.info("🔄 Database quasi vuoto, avvio popolamento dati storici...");
            
            // Esegui in un thread separato per non bloccare l'avvio dell'applicazione
            new Thread(() -> {
                try {
                    // Attendi che l'applicazione sia completamente avviata
                    Thread.sleep(5000);
                    
                    logger.info("📈 Popolamento dati storici degli ultimi 7 giorni...");
                    weatherService.populateHistoricalData(7);
                    
                    logger.info("✅ Popolamento dati storici completato!");
                    
                } catch (Exception e) {
                    logger.error("❌ Errore durante il popolamento dati storici: {}", e.getMessage());
                }
            }).start();
            
        } else {
            logger.info("✅ Database già popolato con {} record, skip inizializzazione", dataCount);
        }
        
        logger.info("🌤️ Inizializzazione completata - applicazione meteo pronta!");
    }
}
