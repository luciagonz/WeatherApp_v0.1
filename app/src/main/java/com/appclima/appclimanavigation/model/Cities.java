package com.appclima.appclimanavigation.model;

public class Cities {

    private double longitude;
    private double latitude;

    private String weatherMainInfo;
    private String weatherDescription;

    private double currentTemperature;
    private double maxTemperature;
    private double minTemperature;
    private double feelsTemperature;

    private int pressure;
    private int humidity;
    private int visibility;

    private double windSpeed;
    private double windDegrees;
    private int clouds;

    private int sunrise;
    private int sunset;

    private String name;
    private String country;
    private Integer symbolWeatherID;
    private Integer locationType;


    // Constructor with all atributes in case is needed:
    public Cities(double longitude, double latitude, String weatherMainInfo, String weatherDescription, double currentTemperature, double maxTemperature, double minTemperature, double feelsTemperature, int pressure, int humidity, int visibility, double windSpeed, double windDegrees, int clouds, int sunrise, int sunset, String name, String country, Integer symbolWeatherID, Integer locationType) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.weatherMainInfo = weatherMainInfo;
        this.weatherDescription = weatherDescription;
        this.currentTemperature = currentTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.feelsTemperature = feelsTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDegrees = windDegrees;
        this.clouds = clouds;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.name = name;
        this.country = country;
        this.symbolWeatherID = symbolWeatherID;
        this.locationType = locationType;
    }


    // Empty constructor:
    public Cities() {
    }


    // Getters and setters:
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWeatherMainInfo() {
        return weatherMainInfo;
    }

    public void setWeatherMainInfo(String weatherMainInfo) {
        this.weatherMainInfo = weatherMainInfo;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getFeelsTemperature() {
        return feelsTemperature;
    }

    public void setFeelsTemperature(double feelsTemperature) {
        this.feelsTemperature = feelsTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(double windDegrees) {
        this.windDegrees = windDegrees;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSymbolWeatherID() {
        return symbolWeatherID;
    }

    public void setSymbolWeatherID(Integer symbolWeatherID) {
        this.symbolWeatherID = symbolWeatherID;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", weatherMainInfo='" + weatherMainInfo + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", currentTemperature=" + currentTemperature +
                ", maxTemperature=" + maxTemperature +
                ", minTemperature=" + minTemperature +
                ", feelsTemperature=" + feelsTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", windSpeed=" + windSpeed +
                ", windDegrees=" + windDegrees +
                ", clouds=" + clouds +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", symbolWeatherID=" + symbolWeatherID +
                ", locationType=" + locationType +
                '}';
    }


}

