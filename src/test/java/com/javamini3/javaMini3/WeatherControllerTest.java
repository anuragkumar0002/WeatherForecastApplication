package com.javamini3.javaMini3;

import com.javamini3.javaMini3.controller.WeatherController;
import com.javamini3.javaMini3.model.AccuWeather;
import com.javamini3.javaMini3.model.OpenWeather;
import com.javamini3.javaMini3.model.WeatherResponse;
import com.javamini3.javaMini3.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    public WeatherControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeather_ValidData() {

        String city = "Omaha";
        String zip = "68022";
        String countryCode = "US";

        AccuWeather accuWeather = new AccuWeather();
        OpenWeather openWeather = new OpenWeather();
        WeatherResponse weatherResponse = new WeatherResponse();

        when(weatherService.getAccuWeather(city)).thenReturn(accuWeather);
        when(weatherService.getOpenWeather(zip, countryCode)).thenReturn(openWeather);
        when(weatherService.aggregateWeatherData(accuWeather, openWeather)).thenReturn(weatherResponse);

        // Act
        ResponseEntity<?> response = weatherController.getWeather(city, zip, countryCode);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(weatherResponse, response.getBody());

        verify(weatherService, times(1)).getAccuWeather(city);
        verify(weatherService, times(1)).getOpenWeather(zip, countryCode);
        verify(weatherService, times(1)).aggregateWeatherData(accuWeather, openWeather);
    }

    @Test
    public void testGetWeather_InvalidLocationData() {
        // Arrange
        String city = "InvalidCity";
        String zip = "00000";
        String countryCode = "XX";

        when(weatherService.getAccuWeather(city)).thenReturn(null);
        when(weatherService.getOpenWeather(zip, countryCode)).thenReturn(null);

        // Act
        ResponseEntity<?> response = weatherController.getWeather(city, zip, countryCode);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid location data provide", response.getBody());

        verify(weatherService, times(1)).getAccuWeather(city);
        verify(weatherService, times(1)).getOpenWeather(zip, countryCode);
        verify(weatherService, never()).aggregateWeatherData(any(), any());
    }


}
