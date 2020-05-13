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
    private List<String> city_name_forecast;



    public ForecastCity(List<Integer> time, List<String> time_text, List<Double> temp_forecast, List<Double> temp_feels_like_forecast,
                        List<Double> temp_max_forecast, List<Double> temp_min_forecast, List<Integer> pressure_forecast,
                        List<Integer> sea_level_forecast, List<Integer> ground_level_forecast, List<Integer> humidity_forecast,
                        List<String> weatherMainInfo_forecast, List<String> weatherDescription_forecast, List<Integer> weatherIconID_forecast,
                        List<Integer> clouds_forecast, List<Integer> wind_speed_forecast, List<Integer> degree_wind_forecast,
                        List<String> city_name_forecast) {

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
        this.city_name_forecast = city_name_forecast;
    }

    public List<String> getCity_name_forecast() {
        return city_name_forecast;
    }

    public void setCity_name_forecast(List<String> city_name_forecast) {
        this.city_name_forecast = city_name_forecast;
    }

    public List<Integer> getTime() {
        return time;
    }

    public void setTime(List<Integer> time) {
        this.time = time;
    }

    public List<String> getTime_text() {
        return time_text;
    }

    public void setTime_text(List<String> time_text) {
        this.time_text = time_text;
    }

    public List<Double> getTemp_forecast() {
        return temp_forecast;
    }

    public void setTemp_forecast(List<Double> temp_forecast) {
        this.temp_forecast = temp_forecast;
    }

    public List<Double> getTemp_feels_like_forecast() {
        return temp_feels_like_forecast;
    }

    public void setTemp_feels_like_forecast(List<Double> temp_feels_like_forecast) {
        this.temp_feels_like_forecast = temp_feels_like_forecast;
    }

    public List<Double> getTemp_max_forecast() {
        return temp_max_forecast;
    }

    public void setTemp_max_forecast(List<Double> temp_max_forecast) {
        this.temp_max_forecast = temp_max_forecast;
    }

    public List<Double> getTemp_min_forecast() {
        return temp_min_forecast;
    }

    public void setTemp_min_forecast(List<Double> temp_min_forecast) {
        this.temp_min_forecast = temp_min_forecast;
    }

    public List<Integer> getPressure_forecast() {
        return pressure_forecast;
    }

    public void setPressure_forecast(List<Integer> pressure_forecast) {
        this.pressure_forecast = pressure_forecast;
    }

    public List<Integer> getSea_level_forecast() {
        return sea_level_forecast;
    }

    public void setSea_level_forecast(List<Integer> sea_level_forecast) {
        this.sea_level_forecast = sea_level_forecast;
    }

    public List<Integer> getGround_level_forecast() {
        return ground_level_forecast;
    }

    public void setGround_level_forecast(List<Integer> ground_level_forecast) {
        this.ground_level_forecast = ground_level_forecast;
    }

    public List<Integer> getHumidity_forecast() {
        return humidity_forecast;
    }

    public void setHumidity_forecast(List<Integer> humidity_forecast) {
        this.humidity_forecast = humidity_forecast;
    }

    public List<String> getWeatherMainInfo_forecast() {
        return weatherMainInfo_forecast;
    }

    public void setWeatherMainInfo_forecast(List<String> weatherMainInfo_forecast) {
        this.weatherMainInfo_forecast = weatherMainInfo_forecast;
    }

    public List<String> getWeatherDescription_forecast() {
        return weatherDescription_forecast;
    }

    public void setWeatherDescription_forecast(List<String> weatherDescription_forecast) {
        this.weatherDescription_forecast = weatherDescription_forecast;
    }

    public List<Integer> getWeatherIconID_forecast() {
        return weatherIconID_forecast;
    }

    public void setWeatherIconID_forecast(List<Integer> weatherIconID_forecast) {
        this.weatherIconID_forecast = weatherIconID_forecast;
    }

    public List<Integer> getClouds_forecast() {
        return clouds_forecast;
    }

    public void setClouds_forecast(List<Integer> clouds_forecast) {
        this.clouds_forecast = clouds_forecast;
    }

    public List<Integer> getWind_speed_forecast() {
        return wind_speed_forecast;
    }

    public void setWind_speed_forecast(List<Integer> wind_speed_forecast) {
        this.wind_speed_forecast = wind_speed_forecast;
    }

    public List<Integer> getDegree_wind_forecast() {
        return degree_wind_forecast;
    }

    public void setDegree_wind_forecast(List<Integer> degree_wind_forecast) {
        this.degree_wind_forecast = degree_wind_forecast;
    }
}
