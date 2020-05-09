package com.appclima.appclimanavigation.model;

public class CalendarEvents {

    private String titleEvent;
    private String locationEvent;
    private String startHourEvent;
    private String endHourEvent;

    public CalendarEvents(String titleEvent, String locationEvent, String startHourEvent, String endHourEvent) {
        this.titleEvent = titleEvent;
        this.locationEvent = locationEvent;
        this.startHourEvent = startHourEvent;
        this.endHourEvent = endHourEvent;
    }

    public String getTitleEvent() {
        return titleEvent;
    }

    public void setTitleEvent(String titleEvent) {
        this.titleEvent = titleEvent;
    }

    public String getLocationEvent() {
        return locationEvent;
    }

    public void setLocationEvent(String locationEvent) {
        this.locationEvent = locationEvent;
    }

    public String getStartHourEvent() {
        return startHourEvent;
    }

    public void setStartHourEvent(String startHourEvent) {
        this.startHourEvent = startHourEvent;
    }

    public String getEndHourEvent() {
        return endHourEvent;
    }

    public void setEndHourEvent(String endHourEvent) {
        this.endHourEvent = endHourEvent;
    }
}
