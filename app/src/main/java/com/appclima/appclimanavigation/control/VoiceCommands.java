package com.appclima.appclimanavigation.control;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.fragments.VoiceFragment;

import java.util.ArrayList;

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
    public void manageReplayAction (ArrayList<String> speechRecognised) {
        this.speechRecognised = speechRecognised;

        // Check all understanding request:

        for (int i = 0; i < speechRecognised.size(); i++) {


            System.out.println("Recognised speech: " + speechRecognised.get(i));
            speechRecognisedAsTrue = speechRecognised.get(i);

            // WEATHER QUERIES IN ALL LOCATIONS
            if (speechRecognised.get(i).contains("weather") & speechRecognised.get(i).contains("in") & speechRecognised.get(i).contains("locations")) {
                ManagePreferences managePreferences = new ManagePreferences(myContext);
                String prefCities = managePreferences.getPreferences("UserPrefs","citiesNames");

                if(speechRecognised.get(i).contains("tomorrow")) {
                    fragmentDisplayed = "weather";
                    speechReplayed = "Weather in your locations for tomorrow are...";
                    isSpeechRecognised = true;
                    break;
                }

                else if(speechRecognised.get(i).contains("forecast")) {
                    fragmentDisplayed = "weather";
                    speechReplayed = "Here is your weather screen with all the information about forecast in your cities: " + prefCities;
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "weather";
                    speechReplayed = "Weather in your locations are these";
                    isSpeechRecognised = true;
                    break;
                }

            }


            // WEATHER QUERIES IN CERTAIN LOCATION
            else if (speechRecognised.get(i).contains("weather") & speechRecognised.get(i).contains("in") & !speechRecognised.get(i).contains("default")) {

                cityRequest = getLocationFromRequest(speechRecognised.get(i), "in");
                fragmentDisplayed = "weather";
                APIWeather apiWeather = new APIWeather(cityRequest, 3, myContext);
                apiWeather.manageInformationRequest(false);

                if(speechRecognised.get(i).contains("tomorrow")) {
                    speechReplayed = "Weather in " + cityRequest +  " for tomorrow is " + apiWeather.getMostCommonTimeDescription(1) + ". Average temperature will be " +
                                    apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " humidity.";
                    isSpeechRecognised = true;
                    break;
                }


                else if(speechRecognised.get(i).contains("forecast")) {
                    apiWeather.averageTemperatureForecast();
                    speechReplayed = "Today is " + apiWeather.getMostCommonTimeDescription(0) + ",  average temperature " + apiWeather.getAverageTemperature(0) + " and " + apiWeather.getAverageHumidity(0) + " average humidity;  \n" +
                            apiWeather.getMostCommonTimeDescription(1) + " for tomorrow, average temperature " + apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(2) + " for " + apiWeather.getDayName(2) + ", average temperature "  + apiWeather.getAverageTemperature(2) + " and " + apiWeather.getAverageHumidity(2) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(3) + " for " + apiWeather.getDayName(3) + ", average temperature "  + apiWeather.getAverageTemperature(3) + " and " + apiWeather.getAverageHumidity(3) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(4) + " for " + apiWeather.getDayName(4) + ", average temperature "  + apiWeather.getAverageTemperature(4) + " and " + apiWeather.getAverageHumidity(4) + " average humidity.";

                    isSpeechRecognised = true;
                    break;
                }

                else {
                    speechReplayed = "Current weather in " + cityRequest +  " is " +  apiWeather.getCurrentTimeDescription() + ". Current temperature is " + apiWeather.getCurrentTemperature()
                                    + " and " + apiWeather.getCurrentHumidity() + " humidity.";
                    Log.d("SpeechReplayed", speechReplayed);
                    isSpeechRecognised = true;
                    break;
                }

            }


            // WEATHER QUERIES IN DEFAULT LOCATION
            else if (speechRecognised.get(i).contains("weather")) {

                ManagePreferences managePreferences = new ManagePreferences(myContext);
                String defaultLocation = managePreferences.getDefaultLocation();
                fragmentDisplayed = "weather";
                APIWeather apiWeather = new APIWeather(defaultLocation, 3, myContext);
                apiWeather.manageInformationRequest(true);

                if(speechRecognised.get(i).contains("tomorrow")) {

                    speechReplayed = "Weather in " + defaultLocation +  " for tomorrow is " + apiWeather.getMostCommonTimeDescription(1) + ". Average temperature will be " +
                            apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " humidity.";
                    isSpeechRecognised = true;
                    break;
                }

                if(speechRecognised.get(i).contains("today")) {

                    speechReplayed = "Current weather in " + defaultLocation +  " is " +  apiWeather.getCurrentTimeDescription() + ". Current temperature is " + apiWeather.getCurrentTemperature()
                            + " and " + apiWeather.getCurrentHumidity() + " humidity.";
                    Log.d("SpeechReplayed", speechReplayed);
                    isSpeechRecognised = true;
                    break;
                }


                else if(speechRecognised.get(i).contains("forecast")) {
                    speechReplayed = "Today is " + apiWeather.getMostCommonTimeDescription(0) + ",  average temperature " + apiWeather.getAverageTemperature(0) + " and " + apiWeather.getAverageHumidity(0) + " average humidity;  \n" +
                            apiWeather.getMostCommonTimeDescription(1) + " for tomorrow, average temperature " + apiWeather.getAverageTemperature(1) + " and " + apiWeather.getAverageHumidity(1) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(2) + " for " + apiWeather.getDayName(2) + ", average temperature "  + apiWeather.getAverageTemperature(2) + " and " + apiWeather.getAverageHumidity(2) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(3) + " for " + apiWeather.getDayName(3) + ", average temperature "  + apiWeather.getAverageTemperature(3) + " and " + apiWeather.getAverageHumidity(3) + " average humidity; \n" +
                            apiWeather.getMostCommonTimeDescription(4) + " for " + apiWeather.getDayName(4) + ", average temperature "  + apiWeather.getAverageTemperature(4) + " and " + apiWeather.getAverageHumidity(4) + " average humidity.";
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
                }

                else if ((speechRecognised.get(i).contains("change") || speechRecognised.get(i).contains("set")) && speechRecognised.get(i).contains("to")) {

                    fragmentDisplayed = "user";
                    cityRequest = getLocationFromRequest(speechRecognised.get(i), "to");
                    ManagePreferences managePreferences = new ManagePreferences(myContext);
                    managePreferences.changeLocation(cityRequest, 2);
                    speechReplayed = "Your default location was changed to" + cityRequest;
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "location";
                    speechReplayed = "Here is your setting screen with your location";
                    isSpeechRecognised = true;
                    continue;
                }

            }

            else if (speechRecognised.get(i).contains("where") || speechRecognised.get(i).contains("place")) {
                fragmentDisplayed = "location";
                locationManagerService.getMyLastCoordinates();
                String myPlace = locationManagerService.getMyPlace();
                speechReplayed = "You are in " + myPlace;
                isSpeechRecognised = true;

            }



            // CALENDAR QUERIES

            // TODO Extra: Adding weather to the calendar: Asking for the weather forecast for tomorrow should add silently the weather forecast in your calendar as a full day event.

            // TODO Extra: Setting up recurring events: Adding an event should include repetitions


            else if (speechRecognised.get(i).contains("event") || speechRecognised.get(i).contains("calendar") || speechRecognised.get(i).contains("meeting")|| speechRecognised.get(i).contains("appointment")) {

                if (speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")|| speechRecognised.get(i).contains("show")) {

                    if (speechRecognised.get(i).contains("tomorrow")) {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "This is your calendar for tomorrow";
                        isSpeechRecognised = true;
                        break;
                    }

                    else if (speechRecognised.get(i).contains("today")) {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "This is your calendar for today";
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

                    if (speechRecognised.get(i).contains("tomorrow")) {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "New event for tomorrow created";
                        isSpeechRecognised = true;
                        break;
                    }

                    if (speechRecognised.get(i).contains("today")) {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "New event for today created";
                        isSpeechRecognised = true;
                        break;
                    } else {
                        fragmentDisplayed = "calendar";
                        speechReplayed = "New event created";
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

    public String getLocationFromRequest (String requestText, String previousWord) {

        String location = null;
        ArrayList<String> wordsFromText = new ArrayList<String>();


        for(String word : requestText.split(" ")) {
            wordsFromText.add(word);
            System.out.println(wordsFromText);
        }

        for (int i = 0; i < wordsFromText.size(); i++) {
            if (wordsFromText.get(i).contains(previousWord)) {
                // word "in" found: the next word will be the location
                location = wordsFromText.get(i+1);
                System.out.println("Location position:  " + i+1);
                break;
            }
        }

        System.out.println(wordsFromText);
        System.out.println(location);

        return location;
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
