package com.appclima.appclimanavigation.model;

public class Cities {

    private String name;
    private String country;
    private double currentDegrees;
    private double maxDegrees;
    private double minDegrees;
    private String symbolWeather;

    public Cities(String name, String country, double currentDegrees, double maxDegrees, double minDegrees, String symbolWeather) {
        System.out.println("City added");
        this.name = name;
        this.country = country;
        this.currentDegrees = currentDegrees;
        this.maxDegrees = maxDegrees;
        this.minDegrees = minDegrees;
        this.symbolWeather = symbolWeather;
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

    public String getSymbolWeather() {
        return symbolWeather;
    }

    public void setSymbolWeather(String symbolWeather) {
        this.symbolWeather = symbolWeather;
    }
}
