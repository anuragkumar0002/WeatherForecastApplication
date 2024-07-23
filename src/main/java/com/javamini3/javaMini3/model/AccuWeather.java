package com.javamini3.javaMini3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccuWeather {
    @JsonProperty("WeatherText")
    private String WeatherText;
    @JsonProperty("HasPrecipitation")
    private boolean HasPrecipitation;
    @JsonProperty("PrecipitationType")
    private String PrecipitationType;
    @JsonProperty("IsDayTime")
    private boolean IsDayTime;
    @JsonProperty("Temperature")
    private Temperature Temperature;

    @Data
    public static class Temperature {
        @JsonProperty("Metric")
        private Metric Metric;
    }

    @Data
    public static class Metric {
        @JsonProperty("Value")
        private double Value;
        @JsonProperty("Unit")
        private String Unit;
    }
}
