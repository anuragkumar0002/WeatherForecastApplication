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

import java.util.concurrent.CompletableFuture;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam String zip, @RequestParam String countryCode) {
        try {
            CompletableFuture<AccuWeather> accuWeatherFuture = weatherService.getAccuWeatherAsync(city);
            CompletableFuture<OpenWeather> openWeatherFuture = weatherService.getOpenWeatherAsync(zip, countryCode);

            // Wait for both futures to complete
            CompletableFuture.allOf(accuWeatherFuture, openWeatherFuture).join();

            AccuWeather accuWeather = accuWeatherFuture.get();
            OpenWeather openWeather = openWeatherFuture.get();

            if (accuWeather == null || openWeather == null) {
                return ResponseEntity.badRequest().body("Invalid location data provided");
            }

            WeatherResponse response = weatherService.aggregateWeatherData(accuWeather, openWeather);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the data");
        }
    }
}
