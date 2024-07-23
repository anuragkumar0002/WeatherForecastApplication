package com.javamini3.javaMini3.model;

import lombok.Data;

@Data
public class WeatherResponse {

    private String weatherText;
    private boolean hasPrecipitation;
    private String precipitationType;
    private boolean isDayTime;
    private double temperature;


    private double feelsLike;
    private int pressure;
    private int humidity;
    private int visibility;
    private double windSpeed;
    private int windDeg;
    private double windGust;
    private long sunrise;
    private long sunset;
}
