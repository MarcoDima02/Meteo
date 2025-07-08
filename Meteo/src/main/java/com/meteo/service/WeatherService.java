package com.meteo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.meteo.dto.GeocodingResponse;
import com.meteo.dto.OpenMeteoResponse;
import com.meteo.dto.WeatherResponse;
import com.meteo.entity.WeatherData;
import com.meteo.repository.WeatherDataRepository;

@Service
public class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    
    // Principali città italiane con coordinate
    private static final Map<String, double[]> ITALIAN_CITIES = new HashMap<>();
    
    static {
        ITALIAN_CITIES.put("Roma", new double[]{41.9028, 12.4964});
        ITALIAN_CITIES.put("Milano", new double[]{45.4642, 9.1900});
        ITALIAN_CITIES.put("Napoli", new double[]{40.8518, 14.2681});
        ITALIAN_CITIES.put("Torino", new double[]{45.0703, 7.6869});
        ITALIAN_CITIES.put("Palermo", new double[]{38.1157, 13.3615});
        ITALIAN_CITIES.put("Genova", new double[]{44.4056, 8.9463});
        ITALIAN_CITIES.put("Bologna", new double[]{44.4949, 11.3426});
        ITALIAN_CITIES.put("Firenze", new double[]{43.7696, 11.2558});
        ITALIAN_CITIES.put("Bari", new double[]{41.1171, 16.8719});
        ITALIAN_CITIES.put("Catania", new double[]{37.5079, 15.0830});
        ITALIAN_CITIES.put("Venezia", new double[]{45.4408, 12.3155});
        ITALIAN_CITIES.put("Verona", new double[]{45.4384, 10.9916});
        ITALIAN_CITIES.put("Messina", new double[]{38.1938, 15.5540});
        ITALIAN_CITIES.put("Padova", new double[]{45.4064, 11.8768});
        ITALIAN_CITIES.put("Trieste", new double[]{45.6495, 13.7768});
        ITALIAN_CITIES.put("Brescia", new double[]{45.5416, 10.2118});
        ITALIAN_CITIES.put("Parma", new double[]{44.8015, 10.3279});
        ITALIAN_CITIES.put("Prato", new double[]{43.8777, 11.1023});
        ITALIAN_CITIES.put("Modena", new double[]{44.6471, 10.9252});
        ITALIAN_CITIES.put("Reggio Calabria", new double[]{38.1102, 15.6615});
    }
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    
    @Value("${weather.api.base-url}")
    private String baseUrl;
    
    @Value("${weather.geocoding.base-url}")
    private String geocodingUrl;
    
    private final WebClient webClient;
    
    public WeatherService() {
        this.webClient = WebClient.builder().build();
    }
    
    /**
     * Ottiene i dati meteo correnti per una città
     */
    public WeatherResponse getCurrentWeather(String cityName) {
        try {
            // Prima controlla se abbiamo dati recenti nel database (meno di 10 minuti)
            Optional<WeatherData> recentData = weatherDataRepository
                .findFirstByCityNameOrderByTimestampDesc(cityName);
            
            if (recentData.isPresent()) {
                WeatherData data = recentData.get();
                if (data.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(10))) {
                    return convertToResponse(data);
                }
            }
            
            // Se non abbiamo dati recenti, recupera dall'API
            WeatherData freshData = fetchWeatherFromAPI(cityName);
            if (freshData != null) {
                weatherDataRepository.save(freshData);
                return convertToResponse(freshData);
            }
            
            // Se l'API fallisce, restituisce i dati dal database se disponibili
            if (recentData.isPresent()) {
                return convertToResponse(recentData.get());
            }
            
            throw new RuntimeException("Nessun dato meteo disponibile per " + cityName);
            
        } catch (Exception e) {
            logger.error("Errore nel recupero dati meteo per {}: {}", cityName, e.getMessage());
            throw new RuntimeException("Errore nel recupero dati meteo: " + e.getMessage());
        }
    }
    
    /**
     * Ottiene i dati meteo per tutte le città principali italiane
     */
    public List<WeatherResponse> getAllItalianCitiesWeather() {
        List<WeatherData> latestData = weatherDataRepository.findLatestWeatherForAllCities();
        
        // Se abbiamo dati recenti per tutte le città, restituisci quelli
        if (latestData.size() >= ITALIAN_CITIES.size()) {
            return latestData.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        }
        
        // Altrimenti aggiorna i dati
        return ITALIAN_CITIES.keySet().stream()
            .map(this::getCurrentWeather)
            .collect(Collectors.toList());
    }
    
    /**
     * Recupera dati storici per una città
     */
    public List<WeatherResponse> getWeatherHistory(String cityName, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<WeatherData> historicalData = weatherDataRepository
            .findByCityNameAndTimestampBetweenOrderByTimestampDesc(
                cityName, since, LocalDateTime.now());
        
        return historicalData.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Task schedulato per aggiornare automaticamente i dati meteo
     */
    @Scheduled(fixedRateString = "${weather.update.interval}")
    public void updateWeatherDataScheduled() {
        logger.info("Aggiornamento automatico dati meteo iniziato");
        
        for (String city : ITALIAN_CITIES.keySet()) {
            try {
                WeatherData weatherData = fetchWeatherFromAPI(city);
                if (weatherData != null) {
                    weatherDataRepository.save(weatherData);
                    logger.debug("Dati aggiornati per {}", city);
                }
                
                // Pausa tra le chiamate API per evitare rate limiting
                Thread.sleep(1000);
                
            } catch (Exception e) {
                logger.warn("Errore nell'aggiornamento dati per {}: {}", city, e.getMessage());
            }
        }
        
        // Pulisci dati vecchi (più di 7 giorni)
        weatherDataRepository.deleteByTimestampBefore(LocalDateTime.now().minusDays(7));
        
        logger.info("Aggiornamento automatico dati meteo completato");
    }
    
    /**
     * Recupera i dati dall'API Open Meteo
     */
    private WeatherData fetchWeatherFromAPI(String cityName) {
        try {
            double[] coordinates = ITALIAN_CITIES.get(cityName);
            if (coordinates == null) {
                // Se non abbiamo le coordinate predefinite, prova con il geocoding
                coordinates = getCoordinatesFromGeocoding(cityName);
                if (coordinates == null) {
                    logger.warn("Coordinate non trovate per {}", cityName);
                    return null;
                }
            }
            
            double lat = coordinates[0];
            double lon = coordinates[1];
            
            String url = String.format(Locale.US, "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,surface_pressure,weather_code&timezone=Europe/Rome", 
                baseUrl, lat, lon);
            
            OpenMeteoResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(OpenMeteoResponse.class)
                .block();
            
            if (response != null && response.getCurrent() != null) {
                return convertToEntity(response, cityName);
            }
            
        } catch (Exception e) {
            logger.error("Errore nella chiamata API per {}: {}", cityName, e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Ottiene le coordinate di una città tramite geocoding
     */
    private double[] getCoordinatesFromGeocoding(String cityName) {
        try {
            String url = String.format("%s?name=%s&count=1&language=it&format=json", 
                geocodingUrl, cityName);
            
            GeocodingResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeocodingResponse.class)
                .block();
            
            if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                GeocodingResponse.GeoLocation location = response.getResults().get(0);
                return new double[]{location.getLatitude(), location.getLongitude()};
            }
            
        } catch (Exception e) {
            logger.error("Errore nel geocoding per {}: {}", cityName, e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Converte la risposta Open Meteo in entità database
     */
    private WeatherData convertToEntity(OpenMeteoResponse apiResponse, String cityName) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(cityName);
        weatherData.setRegion("Italia");
        
        OpenMeteoResponse.CurrentWeather current = apiResponse.getCurrent();
        weatherData.setTemperature(current.getTemperature());
        weatherData.setFeelsLike(current.getApparentTemperature());
        weatherData.setHumidity(current.getHumidity());
        weatherData.setPressure(current.getPressure() != null ? current.getPressure().intValue() : 1013);
        weatherData.setWindSpeed(current.getWindSpeed());
        
        // Converte il weather code di Open Meteo in descrizione e icona
        String[] weatherInfo = convertWeatherCode(current.getWeatherCode());
        weatherData.setDescription(weatherInfo[0]);
        weatherData.setIcon(weatherInfo[1]);
        
        weatherData.setTimestamp(LocalDateTime.now());
        
        return weatherData;
    }
    
    /**
     * Converte i codici meteo di Open Meteo in descrizioni e icone
     */
    private String[] convertWeatherCode(Integer weatherCode) {
        if (weatherCode == null) {
            return new String[]{"Sconosciuto", "01d"};
        }
        
        switch (weatherCode) {
            case 0: return new String[]{"Sereno", "01d"};
            case 1: return new String[]{"Prevalentemente sereno", "02d"};
            case 2: return new String[]{"Parzialmente nuvoloso", "03d"};
            case 3: return new String[]{"Coperto", "04d"};
            case 45: case 48: return new String[]{"Nebbia", "50d"};
            case 51: case 53: case 55: return new String[]{"Pioviggine", "09d"};
            case 56: case 57: return new String[]{"Pioviggine ghiacciata", "09d"};
            case 61: case 63: case 65: return new String[]{"Pioggia", "10d"};
            case 66: case 67: return new String[]{"Pioggia ghiacciata", "10d"};
            case 71: case 73: case 75: return new String[]{"Neve", "13d"};
            case 77: return new String[]{"Granelli di neve", "13d"};
            case 80: case 81: case 82: return new String[]{"Rovesci", "09d"};
            case 85: case 86: return new String[]{"Rovesci di neve", "13d"};
            case 95: return new String[]{"Temporale", "11d"};
            case 96: case 99: return new String[]{"Temporale con grandine", "11d"};
            default: return new String[]{"Vario", "02d"};
        }
    }
    
    /**
     * Converte l'entità database in risposta DTO
     */
    private WeatherResponse convertToResponse(WeatherData weatherData) {
        return new WeatherResponse(
            weatherData.getCityName(),
            weatherData.getRegion(),
            weatherData.getTemperature(),
            weatherData.getFeelsLike(),
            weatherData.getDescription(),
            weatherData.getIcon(),
            weatherData.getHumidity(),
            weatherData.getWindSpeed(),
            weatherData.getPressure(),
            weatherData.getTimestamp()
        );
    }
}
