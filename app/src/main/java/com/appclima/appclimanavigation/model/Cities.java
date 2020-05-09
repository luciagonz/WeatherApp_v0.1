package com.appclima.appclimanavigation.model;

public class Cities {

    private String name;
    private String country;
    private double currentDegrees;
    private double maxDegrees;
    private double minDegrees;
    private Integer symbolWeatherID;
    private Integer locationType;



    public Cities(String name, String country, double currentDegrees, double maxDegrees, double minDegrees, Integer symbolWeatherID, Integer locationType) {
        System.out.println("City added");
        this.name = name;
        this.country = country;
        this.currentDegrees = currentDegrees;
        this.maxDegrees = maxDegrees;
        this.minDegrees = minDegrees;
        this.symbolWeatherID = symbolWeatherID;
        this.locationType = locationType;
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

    public double getCurrentDegrees() {
        return currentDegrees;
    }

    public void setCurrentDegrees(double currentDegrees) {
        this.currentDegrees = currentDegrees;
    }

    public double getMaxDegrees() {
        return maxDegrees;
    }

    public void setMaxDegrees(double maxDegrees) {
        this.maxDegrees = maxDegrees;
    }

    public double getMinDegrees() {
        return minDegrees;
    }

    public void setMinDegrees(double minDegrees) {
        this.minDegrees = minDegrees;
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
}

