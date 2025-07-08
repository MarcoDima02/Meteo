package com.meteo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    /**
     * Pagina principale dell'applicazione
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * Pagina dettagli città
     */
    @GetMapping("/city")
    public String cityDetails() {
        return "city-details";
    }
    
    /**
     * Pagina storico meteo
     */
    @GetMapping("/history")
    public String history() {
        return "history";
    }

    /**
     * Pagina storico meteo per singola città
     */
    @GetMapping("/city-history")
    public String cityHistory() {
        return "city-history";
    }
}
