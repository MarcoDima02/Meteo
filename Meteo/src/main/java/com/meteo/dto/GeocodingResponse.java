package com.meteo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResponse {
    @JsonProperty("results")
    private List<GeoLocation> results;
    
    public List<GeoLocation> getResults() {
        return results;
    }
    
    public void setResults(List<GeoLocation> results) {
        this.results = results;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoLocation {
        @JsonProperty("latitude")
        private Double latitude;
        
        @JsonProperty("longitude")
        private Double longitude;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("country")
        private String country;
        
        @JsonProperty("admin1")
        private String region;
        
        // Getters e Setters
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
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public String getRegion() {
            return region;
        }
        
        public void setRegion(String region) {
            this.region = region;
        }
    }
}
