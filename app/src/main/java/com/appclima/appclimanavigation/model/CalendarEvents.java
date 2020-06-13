package com.appclima.appclimanavigation.model;

import java.util.ArrayList;



public class CalendarEvents {


    private  ArrayList<String> nameOfEvent;
    private  ArrayList<String> startDates;
    private  ArrayList<String> endDates;
    private  ArrayList<String> descriptions;
    private  ArrayList<Integer> calendarID;
    private   ArrayList<String> eventLocation;
    private   ArrayList<Boolean> allDayFlagEvent;
    private   ArrayList<Integer> eventColor;
    private ArrayList<String> weatherDescriptionEvent;
    private ArrayList<Integer> eventID;
    private ArrayList<String> calendarName;

    public CalendarEvents(ArrayList<String> nameOfEvent,
                          ArrayList<String> startDates,
                          ArrayList<String> endDates,
                          ArrayList<String> descriptions,
                          ArrayList<Integer> calendarID,
                          ArrayList<String> eventLocation,
                          ArrayList<Boolean> allDayFlagEvent,
                          ArrayList<Integer> eventColor,
                          ArrayList<String> weatherDescriptionEvent,
                          ArrayList<Integer> eventID,
                          ArrayList<String> calendarName)
    {
        this.nameOfEvent = nameOfEvent;
        this.startDates = startDates;
        this.endDates = endDates;
        this.descriptions = descriptions;
        this.calendarID = calendarID;
        this.eventLocation = eventLocation;
        this.allDayFlagEvent = allDayFlagEvent;
        this.eventColor = eventColor;
        this.weatherDescriptionEvent = weatherDescriptionEvent;
        this.eventID = eventID;
        this.calendarName = calendarName;
    }

    public ArrayList<String> getWeatherDescriptionEvent() {
        return weatherDescriptionEvent;
    }

    public void setWeatherDescriptionEvent(ArrayList<String> weatherDescriptionEvent) {
        this.weatherDescriptionEvent = weatherDescriptionEvent;
    }

    public ArrayList<String> getNameOfEvent() {
        return nameOfEvent;
    }

    public void setNameOfEvent(ArrayList<String> nameOfEvent) {
        this.nameOfEvent = nameOfEvent;
    }

    public ArrayList<String> getStartDates() {
        return startDates;
    }

    public void setStartDates(ArrayList<String> startDates) {
        this.startDates = startDates;
    }

    public ArrayList<String> getEndDates() {
        return endDates;
    }

    public void setEndDates(ArrayList<String> endDates) {
        this.endDates = endDates;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public ArrayList<Integer> getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(ArrayList<Integer> calendarID) {
        this.calendarID = calendarID;
    }

    public ArrayList<String> getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(ArrayList<String> eventLocation) {
        this.eventLocation = eventLocation;
    }

    public ArrayList<Boolean> getAllDayFlagEvent() {
        return allDayFlagEvent;
    }

    public void setAllDayFlagEvent(ArrayList<Boolean> allDayFlagEvent) {
        this.allDayFlagEvent = allDayFlagEvent;
    }

    public ArrayList<Integer> getEventColor() {
        return eventColor;
    }

    public void setEventColor(ArrayList<Integer> eventColor) {
        this.eventColor = eventColor;
    }

    public ArrayList<Integer> getEventID() {
        return eventID;
    }

    public void setEventID(ArrayList<Integer> eventID) {
        this.eventID = eventID;
    }

    public ArrayList<String> getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(ArrayList<String> calendarName) {
        this.calendarName = calendarName;
    }

    @Override
    public String toString() {
        return "CalendarEvents{" +
                "nameOfEvent=" + nameOfEvent +
                ", startDates=" + startDates +
                ", endDates=" + endDates +
                ", descriptions=" + descriptions +
                ", calendarID=" + calendarID +
                ", eventLocation=" + eventLocation +
                ", allDayFlagEvent=" + allDayFlagEvent +
                ", eventColor=" + eventColor +
                ", weatherDescriptionEvent=" + weatherDescriptionEvent +
                ", eventID=" + eventID +
                ", calendarName=" + calendarName +
                '}';
    }
}
