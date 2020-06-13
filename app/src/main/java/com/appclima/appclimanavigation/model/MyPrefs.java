package com.appclima.appclimanavigation.model;

public class MyPrefs {

    // Settings information (user prefs)
    private char temperatureUnit; // C: ºC, K: ºK
    private String username;
    private String windUnit; // km/h or m/s
    private boolean locationUpdateEnabled;
    private boolean darkThemeEnabled;
    private boolean umbrellaReminderEnabled;
    private String startOfWeek;

    public MyPrefs(char temperatureUnit, String username, String windUnit, boolean locationUpdateEnabled, boolean darkThemeEnabled, boolean umbrellaReminderEnabled, String startOfWeek) {
        this.temperatureUnit = temperatureUnit;
        this.username = username;
        this.windUnit = windUnit;
        this.locationUpdateEnabled = locationUpdateEnabled;
        this.darkThemeEnabled = darkThemeEnabled;
        this.umbrellaReminderEnabled = umbrellaReminderEnabled;
        this.startOfWeek = startOfWeek;
    }

    public MyPrefs() {
    }

    public char getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(char temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWindUnit() {
        return windUnit;
    }

    public void setWindUnit(String windUnit) {
        this.windUnit = windUnit;
    }

    public boolean isLocationUpdateEnabled() {
        return locationUpdateEnabled;
    }

    public void setLocationUpdateEnabled(boolean locationUpdateEnabled) {
        this.locationUpdateEnabled = locationUpdateEnabled;
    }

    public boolean isDarkThemeEnabled() {
        return darkThemeEnabled;
    }

    public void setDarkThemeEnabled(boolean darkThemeEnabled) {
        this.darkThemeEnabled = darkThemeEnabled;
    }

    public boolean isUmbrellaReminderEnabled() {
        return umbrellaReminderEnabled;
    }

    public void setUmbrellaReminderEnabled(boolean umbrellaReminderEnabled) {
        this.umbrellaReminderEnabled = umbrellaReminderEnabled;
    }

    public String getStartOfWeek() {
        return startOfWeek;
    }

    public void setStartOfWeek(String startOfWeek) {
        this.startOfWeek = startOfWeek;
    }
}
