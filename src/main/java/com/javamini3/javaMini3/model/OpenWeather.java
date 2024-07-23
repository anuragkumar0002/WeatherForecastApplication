package com.javamini3.javaMini3.model;

import lombok.Data;

@Data
public class OpenWeather {

        private Coord coord;
        private Weather[] weather;
        private Main main;
        private Wind wind;
        private Sys sys;
        private long dt;
        private String name;
        private int visibility;

        @Data
        public static class Coord {
            private double lon;
            private double lat;
        }

        @Data
        public static class Weather {
            private int id;
            private String main;
            private String description;
            private String icon;
        }

        @Data
        public static class Main {
            private double temp;
            private double feels_like;
            private int pressure;
            private int humidity;
        }

        @Data
        public static class Wind {
            private double speed;
            private int deg;
            private double gust;
        }

        @Data
        public static class Sys {
            private int type;
            private int id;
            private String country;
            private long sunrise;
            private long sunset;
        }
    }

