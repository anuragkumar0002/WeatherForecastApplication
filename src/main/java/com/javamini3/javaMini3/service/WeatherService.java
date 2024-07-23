package com.javamini3.javaMini3.service;
import com.javamini3.javaMini3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


@Service
public class WeatherService {

    @Value("${accuweather.apikey}")
    private String accuweatherApiKey;

    @Value("${openweather.apikey}")
    private String openWeatherApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public AccuWeather getAccuWeather(String city) {
        try {

            String locationUrl = "https://dataservice.accuweather.com/locations/v1/search?q=" + city + "&apikey=" + accuweatherApiKey;
//        String jsonResponse = restTemplate.getForObject(locationUrl, String.class);
//        System.out.println(jsonResponse);

            AccuWeatherLocation[] locationResponses = restTemplate.getForObject(locationUrl, AccuWeatherLocation[].class);


            if (locationResponses != null && locationResponses.length > 0) {
                String locationKey = locationResponses[0].getKey();
                System.out.println(locationKey);

                if (locationKey != null) {
                    String weatherUrl = "https://dataservice.accuweather.com/currentconditions/v1/" + locationKey + "?apikey=" + accuweatherApiKey;
                    System.out.println("Weather URL: " + weatherUrl);
                    AccuWeather[] weatherResponses = restTemplate.getForObject(weatherUrl, AccuWeather[].class);
                    System.out.println("Weather response: " + Arrays.toString(weatherResponses));
                    if (weatherResponses != null && weatherResponses.length > 0) {
                        return weatherResponses[0];
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error fetching AccuWeather data: " + e.getMessage());
        }
        System.out.println("No valid location key found.");
        return null;
    }


    public OpenWeather getOpenWeather(String zip, String countryCode) {
        try {
            String geoUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zip + "," + countryCode + "&appid=" + openWeatherApiKey;
            OpenWeatherGeo geoResponse = restTemplate.getForObject(geoUrl, OpenWeatherGeo.class);
            if (geoResponse != null) {
                String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + geoResponse.getLat() + "&lon=" + geoResponse.getLon() + "&appid=" + openWeatherApiKey;
                return restTemplate.getForObject(weatherUrl, OpenWeather.class);
            }
        } catch (Exception e) {
            System.out.println("Error fetching OpenWeather data: " + e.getMessage());
        }

        return null;
    }


    public WeatherResponse aggregateWeatherData(AccuWeather accuWeather, OpenWeather openWeather) {
        WeatherResponse response = new WeatherResponse();
        if (accuWeather != null) {
            response.setWeatherText(accuWeather.getWeatherText());
            response.setHasPrecipitation(accuWeather.isHasPrecipitation());
            response.setPrecipitationType(accuWeather.getPrecipitationType());
            response.setDayTime(accuWeather.isIsDayTime());
            response.setTemperature(accuWeather.getTemperature().getMetric().getValue());
        }

        if (openWeather != null) {
            response.setFeelsLike(openWeather.getMain().getFeels_like() - 273.15);
            response.setPressure(openWeather.getMain().getPressure());
            response.setHumidity(openWeather.getMain().getHumidity());
            response.setVisibility(openWeather.getVisibility());
            response.setWindSpeed(openWeather.getWind().getSpeed());
            response.setWindDeg(openWeather.getWind().getDeg());
            response.setWindGust(openWeather.getWind().getGust());
            response.setSunrise(openWeather.getSys().getSunrise());
            response.setSunset(openWeather.getSys().getSunset());
        }

        return response;
    }
}