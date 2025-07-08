package com.meteo.dto;

import java.time.LocalDateTime;

public class WeatherResponse {
    private String cityName;
    private String region;
    private Double temperature;
    private Double feelsLike;
    private String description;
    private String icon;
    private Integer humidity;
    private Double windSpeed;
    private Integer pressure;
    private LocalDateTime timestamp;
    
    // Costruttori
    public WeatherResponse() {}
    
    public WeatherResponse(String cityName, String region, Double temperature, Double feelsLike,
                          String description, String icon, Integer humidity, Double windSpeed,
                          Integer pressure, LocalDateTime timestamp) {
        this.cityName = cityName;
        this.region = region;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.description = description;
        this.icon = icon;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.timestamp = timestamp;
    }
    
    // Getters e Setters
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Double getFeelsLike() {
        return feelsLike;
    }
    
    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getHumidity() {
        return humidity;
    }
    
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
    
    public Double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public Integer getPressure() {
        return pressure;
    }
    
    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
