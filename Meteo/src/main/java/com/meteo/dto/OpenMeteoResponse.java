package com.meteo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoResponse {
    
    @JsonProperty("current")
    private CurrentWeather current;
    
    @JsonProperty("latitude")
    private Double latitude;
    
    @JsonProperty("longitude")
    private Double longitude;
    
    @JsonProperty("timezone")
    private String timezone;
    
    // Getters e Setters
    public CurrentWeather getCurrent() {
        return current;
    }
    
    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {
        @JsonProperty("temperature_2m")
        private Double temperature;
        
        @JsonProperty("apparent_temperature")
        private Double apparentTemperature;
        
        @JsonProperty("relative_humidity_2m")
        private Integer humidity;
        
        @JsonProperty("wind_speed_10m")
        private Double windSpeed;
        
        @JsonProperty("surface_pressure")
        private Double pressure;
        
        @JsonProperty("weather_code")
        private Integer weatherCode;
        
        @JsonProperty("time")
        private String time;
        
        // Getters e Setters
        public Double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
        
        public Double getApparentTemperature() {
            return apparentTemperature;
        }
        
        public void setApparentTemperature(Double apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
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
        
        public Double getPressure() {
            return pressure;
        }
        
        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }
        
        public Integer getWeatherCode() {
            return weatherCode;
        }
        
        public void setWeatherCode(Integer weatherCode) {
            this.weatherCode = weatherCode;
        }
        
        public String getTime() {
            return time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
    }
}
