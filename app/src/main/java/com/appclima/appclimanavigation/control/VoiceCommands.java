package com.appclima.appclimanavigation.control;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.fragments.HomeFragment;
import com.appclima.appclimanavigation.presentation.fragments.VoiceFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VoiceCommands extends MainActivity {
    private String speechReplayed;
    private String fragmentDisplayed;
    private ArrayList<String> speechRecognised;
    private String speechRecognisedAsTrue;
    private boolean isSpeechRecognised;
    private String cityRequest = null;
    private Activity myActivity;
    private Context myContext;
    private String queryWeatherAPI;
    private ManageLocation locationManagerService;


    // Constructor with input argument
    public VoiceCommands(Context myContext, Activity myActivity, ManageLocation locationService) {
        this.myActivity = myActivity;
        this.myContext = myContext;
        this.locationManagerService = locationService;
        System.out.println(this.speechRecognised);
    }


    // This method decides the replay from speech recognised
    public void manageReplayAction (ArrayList<String> speechRecognised) throws ParseException {
        this.speechRecognised = speechRecognised;

        // Check all understanding request:

        for (int i = 0; i < speechRecognised.size(); i++) {


            System.out.println("Recognised speech: " + speechRecognised.get(i));
            speechRecognisedAsTrue = speechRecognised.get(i);

            // WEATHER QUERIES IN ALL LOCATIONS
            if (speechRecognised.get(i).contains("weather") & speechRecognised.get(i).contains("in") & speechRecognised.get(i).contains("locations")) {
                ManagePreferences managePreferences = new ManagePreferences(myContext);
                String prefCities = managePreferences.getPreferences("UserPrefs", "citiesNames");
                List<String> cityListNames = Arrays.asList(prefCities.split(","));

                ArrayList<String> cityList = new ArrayList<>();
                ArrayList<String> cityDescription = new ArrayList<>();

                if (speechRecognised.get(i).contains("tomorrow")) {

                    if (prefCities.length()>1) {
                        for (int j = 0; j < cityListNames.size(); j++) {

                            APIWeather apiWeather = new APIWeather(cityListNames.get(j), myContext);
                            boolean isCityInformationCorrect = apiWeather.manageInformationRequest(true);

                            if (isCityInformationCorrect) {
                                cityList.add(cityListNames.get(j));
                                cityDescription.add(apiWeather.getMostCommonTimeDescription(1));
                            }

                        }

                        Log.d("Current cities:", cityList.toString());
                        Log.d("Weather descrip.:", cityDescription.toString());

                    }


                    fragmentDisplayed = "weather";
                    speechReplayed = "Weather in your locations for tomorrow are...";
                    isSpeechRecognised = true;

                    break;

                } else if (speechRecognised.get(i).contains("forecast")) {
                    fragmentDisplayed = "weather";
                    speechReplayed = "Here is your weather screen with all the information about forecast in your cities: " + prefCities;
                    isSpeechRecognised = true;
                    break;

                } else {

                    if (prefCities.length()>1) {
                        for (int j = 0; j < cityListNames.size(); j++) {

                            APIWeather apiWeather = new APIWeather(cityListNames.get(j), myContext);
                            boolean isCityInformationCorrect = apiWeather.manageInformationRequest(true);

                            if (isCityInformationCorrect) {
                                cityList.add(cityListNames.get(j));
                                cityDescription.add(apiWeather.getCurrentTimeDescription());
                            }

                        }

                        Log.d("Current cities:", cityList.toString());
                        Log.d("Weather descrip.:", cityDescription.toString());

                    }

                    fragmentDisplayed = "weather";
                    speechReplayed = "Weather in your locations are these";
                    isSpeechRecognised = true;
                    break;
                }

            }


            // WEATHER QUERIES IN CERTAIN LOCATION
            else if (speechRecognised.get(i).contains("weather") & speechRecognised.get(i).contains("in") & !speechRecognised.get(i).contains("default")) {

                ArrayList<String> arrayCities = getArrayLocationFromRequest(speechRecognised.get(i), "in ", " for ");
                APIWeather apiWeather = new APIWeather("", 3, myContext);
                boolean validCity = false;


                // Cities with more than 1 word:
                for (int j = (arrayCities.size() - 1); j >= 0; j--) {
                    System.out.println("Ciudad evaluada: " + arrayCities.get(j));
                    cityRequest = arrayCities.get(j);
                    apiWeather.setCityName(cityRequest);
                    validCity = apiWeather.manageInformationRequest(false);
                    System.out.println("Valid request with " + cityRequest + ": " + validCity);
                    if (validCity) {
                        break;
                    }
                }

                if (validCity) {
                    System.out.println("Validated city: " + cityRequest);
                    fragmentDisplayed = "weather";
                    ManagePreferences managePreferences = new ManagePreferences(myContext);
                    managePreferences.changeLocation(cityRequest, 3);
                    int cityPosition = managePreferences.getCityPosition(cityRequest);
                    System.out.println("city position : " + cityPosition);
                    managePreferences.setManagerLayoutPosition(cityPosition);


                    if (speechRecognised.get(i).contains("tomorrow")) {
                        speechReplayed = "Weather in " + cityRequest + " for tomorrow is " + apiWeather.getMostCommonTimeDescription(1) + ". Average temperature will be " +
                                apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " humidity.";
                        isSpeechRecognised = true;
                        break;

                    } else if (speechRecognised.get(i).contains("forecast")) {
                        apiWeather.averageTemperatureForecast();
                        speechReplayed = "Today is " + apiWeather.getMostCommonTimeDescription(0) + ",  average temperature " + apiWeather.getAverageTemperature(0) + " and " + apiWeather.getAverageHumidity(0) + " average humidity;  \n" +
                                apiWeather.getMostCommonTimeDescription(1) + " for tomorrow, average temperature " + apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " average humidity; \n" +
                                apiWeather.getMostCommonTimeDescription(2) + " for " + apiWeather.getDayName(2) + ", average temperature " + apiWeather.getAverageTemperature(2) + " and " + apiWeather.getAverageHumidity(2) + " average humidity; \n" +
                                apiWeather.getMostCommonTimeDescription(3) + " for " + apiWeather.getDayName(3) + ", average temperature " + apiWeather.getAverageTemperature(3) + " and " + apiWeather.getAverageHumidity(3) + " average humidity; \n" +
                                apiWeather.getMostCommonTimeDescription(4) + " for " + apiWeather.getDayName(4) + ", average temperature " + apiWeather.getAverageTemperature(4) + " and " + apiWeather.getAverageHumidity(4) + " average humidity.";

                        isSpeechRecognised = true;
                        break;

                    } else {
                        speechReplayed = "Current weather in " + cityRequest + " is " + apiWeather.getCurrentTimeDescription() + ". Current temperature is " + apiWeather.getCurrentTemperature()
                                + " and " + apiWeather.getCurrentHumidity() + " humidity.";
                        Log.d("SpeechReplayed", speechReplayed);
                        isSpeechRecognised = true;
                        break;

                    }

                } else {
                    fragmentDisplayed = "voice";
                    speechReplayed = "Sorry, I didn't understand you, could you please repeat the question?";
                    isSpeechRecognised = false;
                    continue;
                }
            }

            // WEATHER QUERIES IN DEFAULT LOCATION
            else if (speechRecognised.get(i).contains("weather")) {

                ManagePreferences managePreferences = new ManagePreferences(myContext);
                String defaultLocation = managePreferences.getDefaultLocation();
                fragmentDisplayed = "weather";
                APIWeather apiWeather = new APIWeather(defaultLocation, 3, myContext);
                apiWeather.manageInformationRequest(true);
                managePreferences.setManagerLayoutPosition(1);

                if (speechRecognised.get(i).contains("tomorrow")) {

                    speechReplayed = "Weather in " + defaultLocation + " for tomorrow is " + apiWeather.getMostCommonTimeDescription(1) + ". Average temperature will be " +
                            apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " humidity.";
                    isSpeechRecognised = true;
                    break;
                }

                if (speechRecognised.get(i).contains("today") || speechRecognised.get(i).contains("default")) {

                    speechReplayed = "Current weather in " + defaultLocation + " is " + apiWeather.getCurrentTimeDescription() + ". Current temperature is " + apiWeather.getCurrentTemperature()
                            + " and " + apiWeather.getCurrentHumidity() + " humidity.";
                    Log.d("SpeechReplayed", speechReplayed);
                    isSpeechRecognised = true;
                    break;

                } else if (speechRecognised.get(i).contains("forecast")) {
                    speechReplayed = "Today is " + apiWeather.getMostCommonTimeDescription(0) + ",  average temperature " + apiWeather.getAverageTemperature(0) + " and " + apiWeather.getAverageHumidity(0) + " average humidity;  \n" +
                            apiWeather.getMostCommonTimeDescription(1) + " for tomorrow, average temperature " + apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(2) + " for " + apiWeather.getDayName(2) + ", average temperature " + apiWeather.getAverageTemperature(2) + " and " + apiWeather.getAverageHumidity(2) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(3) + " for " + apiWeather.getDayName(3) + ", average temperature " + apiWeather.getAverageTemperature(3) + " and " + apiWeather.getAverageHumidity(3) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(4) + " for " + apiWeather.getDayName(4) + ", average temperature " + apiWeather.getAverageTemperature(4) + " and " + apiWeather.getAverageHumidity(4) + " average humidity.";
                    isSpeechRecognised = true;
                    break;

                }

                else {
                    fragmentDisplayed = "weather";
                    speechReplayed = "Here is all the weather information about your cities";
                    isSpeechRecognised = true;
                    break;
                }

            }

            // DEFAULT LOCATION QUERIES
            else if (speechRecognised.get(i).contains("location")) {
                if ((speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")) && speechRecognised.get(i).contains("default")) {
                    ManagePreferences managePreferences = new ManagePreferences(myContext);
                    String defaultLocation = managePreferences.getDefaultLocation();
                    fragmentDisplayed = "user";
                    speechReplayed = defaultLocation + " is your default location.";
                    isSpeechRecognised = true;
                    break;
                }

                if (speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")) {
                    fragmentDisplayed = "user";
                    double myLatitude = locationManagerService.getMyLatitude();
                    double myLongitude = locationManagerService.getMyLongitude();

                    speechReplayed = "You are in " + myLatitude + " - " + myLongitude;
                    System.out.println(speechReplayed);
                    isSpeechRecognised = true;
                    break;

                } else if ((speechRecognised.get(i).contains("change") || speechRecognised.get(i).contains("set")) && speechRecognised.get(i).contains("to")) {

                    ArrayList<String> arrayCities = getArrayLocationFromRequest(speechRecognised.get(i), " to ", " for ");
                    APIWeather apiWeather = new APIWeather("", 3, myContext);
                    boolean validCity = false;


                    for (int j = (arrayCities.size() - 1); j >= 0; j--) {
                        System.out.println("Ciudad evaluada: " + arrayCities.get(j));
                        cityRequest = arrayCities.get(j);
                        apiWeather.setCityName(cityRequest);
                        validCity = apiWeather.manageInformationRequest(false);
                        System.out.println("Valid request with " + cityRequest + ": " + validCity);
                        if (validCity) {
                            break;
                        }
                    }

                    if (validCity) {
                        fragmentDisplayed = "user";
                        ManagePreferences managePreferences = new ManagePreferences(myContext);
                        managePreferences.changeLocation(cityRequest, 2);
                        speechReplayed = "Your default location was changed to" + cityRequest;
                        isSpeechRecognised = true;
                        break;

                    } else {
                        fragmentDisplayed = "voice";
                        speechReplayed = "Sorry, I didn't understand you, could you please repeat the question?";
                        isSpeechRecognised = false;
                        continue;
                    }

                } else if ((speechRecognised.get(i).contains("delete") || speechRecognised.get(i).contains("remove")) && speechRecognised.get(i).contains("default")) {
                    fragmentDisplayed = "user";
                    ManagePreferences managePreferences = new ManagePreferences(myContext);
                    managePreferences.changeLocation("defaultLocation", 2);
                    String defaultLocation = managePreferences.getDefaultLocation();
                    int defaultCityPosition = -1;
                    defaultCityPosition = managePreferences.getCityPosition(defaultLocation);
                    managePreferences.removeLocation(defaultCityPosition);
                    speechReplayed = "Your default location was removed";
                    isSpeechRecognised = true;
                    break;

                } else {
                    fragmentDisplayed = "location";
                    speechReplayed = "Here is your setting screen with your location";
                    isSpeechRecognised = true;
                    continue;
                }

            } else if (speechRecognised.get(i).contains("where") || speechRecognised.get(i).contains("place")) {
                fragmentDisplayed = "location";
                locationManagerService.getMyLastCoordinates();
                String myPlace = locationManagerService.getMyPlace();
                speechReplayed = "You are in " + myPlace;
                isSpeechRecognised = true;
                break;

            } else if ((speechRecognised.get(i).contains("delete") || speechRecognised.get(i).contains("remove"))) {
                System.out.println("Remove city");

                ArrayList<String> arrayCities = getArrayLocationFromRequest(speechRecognised.get(i), "delete ", " from ");
                boolean validCity = false;


                for (int j = (arrayCities.size() - 1); j >= 0; j--) {
                    System.out.println("Ciudad evaluada: " + arrayCities.get(j));
                    cityRequest = arrayCities.get(j);
                    APIWeather apiWeather = new APIWeather(cityRequest, 3, myContext);
                    validCity = apiWeather.manageInformationRequest(false);
                    System.out.println("Valid request with " + cityRequest + ": " + validCity);

                    if (validCity) {
                        fragmentDisplayed = "weather";
                        ManagePreferences managePreferences = new ManagePreferences(myContext);
                        int position = -1;
                        position = managePreferences.getCityPosition(cityRequest);
                        System.out.println("City position: " + position);
                        if (position >= 0) {
                            managePreferences.removeLocation(position);
                        }
                        speechReplayed = cityRequest + " was removed";
                        isSpeechRecognised = true;
                        break;
                     }

                    else {

                        fragmentDisplayed = "voice";
                        speechReplayed = "Sorry, I didn't understand you, could you please repeat the question?";
                        isSpeechRecognised = false;
                        continue;

                    }
                }



            }




            // CALENDAR QUERIES

            // TODO Extra: Adding weather to the calendar: Asking for the weather forecast for tomorrow should add silently the weather forecast in your calendar as a full day event.

            // TODO Extra: Setting up recurring events: Adding an event should include repetitions


            else if (speechRecognised.get(i).contains("event") || speechRecognised.get(i).contains("calendar") || speechRecognised.get(i).contains("meeting")|| speechRecognised.get(i).contains("appointment")) {

                if (speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")|| speechRecognised.get(i).contains("show")) {

                    if (speechRecognised.get(i).contains("tomorrow")) {
                        ManageCalendar manageCalendar = new ManageCalendar(myContext, myActivity);
                        SimpleDateFormat today = new SimpleDateFormat("MM/dd/yy");
                        CalendarEvents calendarEventsList;
                        calendarEventsList = manageCalendar.getCalendarEvent(today.format(new Date(new Date().getTime()+ (1000 * 60 * 60 * 24))), today.format(new Date(new Date().getTime()+ (1000 * 60 * 60 * 24))));
                        ArrayList<String> eventsNames = new ArrayList<>(calendarEventsList.getNameOfEvent());
                        if (eventsNames.size() > 0) {
                            String eventsNamesString = eventsNames.toString();
                            eventsNamesString = eventsNamesString.substring(1, eventsNamesString.length() - 1).replace(", ", ",");
                            speechReplayed = "Your events for tomorrow are " + eventsNamesString;
                        }

                        else { speechReplayed = "Good news, tomorrow you're free!"; }

                        fragmentDisplayed = "calendar";
                        isSpeechRecognised = true;
                        break;
                    }

                    else if (speechRecognised.get(i).contains("today")) {
                        ManageCalendar manageCalendar = new ManageCalendar(myContext, myActivity);
                        SimpleDateFormat today = new SimpleDateFormat("MM/dd/yy");
                        CalendarEvents calendarEventsList;
                        calendarEventsList = manageCalendar.getCalendarEvent(today.format(new Date()), today.format(new Date()));
                        ArrayList<String> eventsNames = new ArrayList<>(calendarEventsList.getNameOfEvent());
                        if (eventsNames.size() > 0) {
                            String eventsNamesString = eventsNames.toString();
                            eventsNamesString = eventsNamesString.substring(1, eventsNamesString.length() - 1).replace(", ", ",");
                            speechReplayed = "Your events for today are " + eventsNamesString;
                        }

                        else { speechReplayed = "Good news, today you're free!"; }

                        fragmentDisplayed = "calendar";
                        isSpeechRecognised = true;



                        break;
                    }

                    else {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "This is your calendar for the following days";
                        isSpeechRecognised = true;
                        break;
                    }

                } else if (speechRecognised.get(i).contains("set") || speechRecognised.get(i).contains("create")) {
                    ManagePreferences managePreferences = new ManagePreferences(myContext);
                    String name = "Default title";
                    String description = "Default description";
                    Integer startHour = 0;
                    Integer endHour = 0;
                    String location = managePreferences.getDefaultLocation();
                    String recurringRule = null;
                    boolean allDayEvent = false;

                    if (getAttributeEvent(speechRecognised.get(i), " name ") != null) {
                        name = getAttributeEvent(speechRecognised.get(i), " name ");

                    }

                    if (getAttributeEvent(speechRecognised.get(i), " description ") != null) {
                        description = getAttributeEvent(speechRecognised.get(i), " description ");

                    }

                    if (getAttributeEvent(speechRecognised.get(i), " from ") != null) {
                        startHour = Integer.valueOf(getAttributeEvent(speechRecognised.get(i), " from "));
                    }

                    if (getAttributeEvent(speechRecognised.get(i), " to ") != null) {
                        endHour = Integer.valueOf(getAttributeEvent(speechRecognised.get(i), " to "));
                    }

                    if (getAttributeEvent(speechRecognised.get(i), " in ") != null) {
                        location = getAttributeEvent(speechRecognised.get(i), " in ");
                    }

                    if (speechRecognised.get(i).contains("recurring")) {
                        recurringRule = "FREQ=DAILY;WKST=SU";
                        name = "Recurring event";
                    }

                    if ((speechRecognised.get(i).contains("all")) && (speechRecognised.get(i).contains("day"))) {
                        allDayEvent = true;
                        name = "All day event";
                    }


                    final Calendar startDate = Calendar.getInstance();
                    final Calendar endDate = Calendar.getInstance();

                    if (startHour>0) {
                        if (endHour>0) {
                            startDate.set(Calendar.HOUR_OF_DAY, startHour);
                            endDate.set(Calendar.HOUR_OF_DAY, endHour);
                            startDate.set(Calendar.MINUTE, 0);
                            endDate.set(Calendar.MINUTE, 0);


                        }

                        else {
                            startDate.set(Calendar.HOUR_OF_DAY, startHour);
                            endDate.set(Calendar.HOUR_OF_DAY, endHour+2);
                        }
                    }

                    else {
                        endDate.add(Calendar.HOUR_OF_DAY, 2);
                    }



                    if (speechRecognised.get(i).contains("tomorrow")) {

                        endDate.add(Calendar.HOUR, 24);
                        startDate.add(Calendar.HOUR, 24);
                        ManageCalendar manageCalendar = new ManageCalendar(myContext, myActivity);
                        manageCalendar.setCreateEventAllDayChecked(allDayEvent);
                        manageCalendar.setCreateEventCalendarAccount(1);
                        System.out.println("Event for tomorrow");
                        manageCalendar.createEvent(startDate, endDate,name, description,location ,recurringRule);
                        fragmentDisplayed = "calendar";
                        speechReplayed = "New event for tomorrow created";
                        isSpeechRecognised = true;
                        break;
                    }

                    if (speechRecognised.get(i).contains("today")) {
                        ManageCalendar manageCalendar = new ManageCalendar(myContext, myActivity);
                        manageCalendar.setCreateEventAllDayChecked(allDayEvent);
                        manageCalendar.setCreateEventCalendarAccount(1);
                        System.out.println("Event for tomorrow");
                        manageCalendar.createEvent(startDate, endDate,name, description,location ,recurringRule);
                        fragmentDisplayed = "calendar";
                        speechReplayed = "New event for today created";
                        isSpeechRecognised = true;
                        break;

                    } else {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "Please, enter event infromation and press OK button to create the event";
                        ManageCalendar manageCalendar = new ManageCalendar(myContext, myActivity);
                        manageCalendar.openNewEventDialogOnClick();

                        isSpeechRecognised = true;
                        break;
                    }

                }
                else {
                    fragmentDisplayed = "calendar";
                    speechReplayed = "Here is your calendar with current events!";
                    isSpeechRecognised = true;
                    continue;
                }
            }



            // UNKNOWN QUERY

            else {
                fragmentDisplayed = "voice";
                speechReplayed = "Sorry, I didn't understand you, could you please repeat the question?";
                isSpeechRecognised = false;
                continue;
            }
        }

    }

    public String getAttributeEvent (String requestText, String keyWord) {
        if(requestText.contains(keyWord)){
            String attribute = null;
            attribute = requestText.split(keyWord)[1].split(" ")[0];
            System.out.println(keyWord + attribute);
            return attribute;
        }

        else {
            return null;
        }
    }

    public ArrayList<String> getArrayLocationFromRequest(String requestText, String firstKeyWord, String secondKeyWord) {
        String cityText = "";
        if(requestText.contains(firstKeyWord)){
            cityText = requestText.split(firstKeyWord)[1].split(secondKeyWord)[0];
        }

        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> cities = new ArrayList<>();


        for(String word : cityText.split(" ")) {
            words.add(word);
            System.out.println(words);
        }

        System.out.println("Words recognised: " + words);


        for (int i = 0; i<words.size(); i++) {
            if (i == 0) {
                cities.add(words.get(i));
            }

            else {
                cities.add(cities.get(i-1) + " " + words.get(i));
            }
        }

        System.out.println("Cities recognised: " + cities);

        return cities;
    }





    // Getters and setters

    public String getSpeechReplayed() {

        return speechReplayed;
    }

    public void setSpeechReplayed(String speechReplayed) {

        this.speechReplayed = speechReplayed;
    }

    public String getFragmentDisplayed() {

        return fragmentDisplayed;
    }



    public void setFragmentDisplayed(String fragmentDisplayed) {

        this.fragmentDisplayed = fragmentDisplayed;

    }

    public String getSpeechRecognisedAsTrue() {
        return speechRecognisedAsTrue;
    }

    public void setSpeechRecognisedAsTrue(String speechRecognisedAsTrue) {
        this.speechRecognisedAsTrue = speechRecognisedAsTrue;
    }

    public ArrayList<String> getSpeechRecognised() {
        return speechRecognised;
    }

    public void setSpeechRecognised(ArrayList<String> speechRecognised) {
        this.speechRecognised = speechRecognised;
    }

    public boolean isSpeechRecognised() {
        return isSpeechRecognised;
    }

    public void setSpeechRecognised(boolean speechRecognised) {
        isSpeechRecognised = speechRecognised;
    }
}
