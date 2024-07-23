package com.javamini3.javaMini3.controller;

import com.javamini3.javaMini3.model.AccuWeather;
import com.javamini3.javaMini3.model.OpenWeather;
import com.javamini3.javaMini3.model.WeatherResponse;
import com.javamini3.javaMini3.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam String zip, @RequestParam String countryCode) {
        try {
            AccuWeather accuWeather = weatherService.getAccuWeather(city);
            OpenWeather openWeather = weatherService.getOpenWeather(zip, countryCode);

            if (accuWeather == null || openWeather == null) {
                return ResponseEntity.badRequest().body("Invalid location data provide");
            }
            System.out.println("AccuWeather: " + accuWeather);
            WeatherResponse response = weatherService.aggregateWeatherData(accuWeather, openWeather);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred whiled fetching the data");
        }
    }

}