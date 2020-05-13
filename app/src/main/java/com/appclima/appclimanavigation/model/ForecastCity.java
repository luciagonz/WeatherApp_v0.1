package com.appclima.appclimanavigation.model;

import java.util.List;

public class ForecastCity {

    // City forecast info:
    private List<Integer> time;
    private List<String> time_text;
    private List<Double> temp_forecast;
    private List<Double> temp_feels_like_forecast;
    private List<Double> temp_max_forecast;
    private List<Double> temp_min_forecast;
    private List<Integer> pressure_forecast;
    private List<Integer> sea_level_forecast;
    private List<Integer> ground_level_forecast;
    private List<Integer> humidity_forecast;
    private List<String>  weatherMainInfo_forecast;
    private List<String>  weatherDescription_forecast;
    private List<Integer>  weatherIconID_forecast;

    private List<Integer>  clouds_forecast;
    private List<Integer>  wind_speed_forecast;
    private List<Integer>  degree_wind_forecast;


    public ForecastCity(List<Integer> time, List<String> time_text, List<Double> temp_forecast, List<Double> temp_feels_like_forecast,
                        List<Double> temp_max_forecast, List<Double> temp_min_forecast, List<Integer> pressure_forecast,
                        List<Integer> sea_level_forecast, List<Integer> ground_level_forecast, List<Integer> humidity_forecast,
                        List<String> weatherMainInfo_forecast, List<String> weatherDescription_forecast, List<Integer> weatherIconID_forecast,
                        List<Integer> clouds_forecast, List<Integer> wind_speed_forecast, List<Integer> degree_wind_forecast) {

        this.time = time;
        this.time_text = time_text;
        this.temp_forecast = temp_forecast;
        this.temp_feels_like_forecast = temp_feels_like_forecast;
        this.temp_max_forecast = temp_max_forecast;
        this.temp_min_forecast = temp_min_forecast;
        this.pressure_forecast = pressure_forecast;
        this.sea_level_forecast = sea_level_forecast;
        this.ground_level_forecast = ground_level_forecast;
        this.humidity_forecast = humidity_forecast;
        this.weatherMainInfo_forecast = weatherMainInfo_forecast;
        this.weatherDescription_forecast = weatherDescription_forecast;
        this.weatherIconID_forecast = weatherIconID_forecast;
        this.clouds_forecast = clouds_forecast;
        this.wind_speed_forecast = wind_speed_forecast;
        this.degree_wind_forecast = degree_wind_forecast;
    }
}
