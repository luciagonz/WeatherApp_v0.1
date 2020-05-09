package com.appclima.appclimanavigation.control;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class VoiceCommands {
    private String speechReplayed;
    private String fragmentDisplayed;
    private ArrayList<String> speechRecognised;
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

            System.out.println(speechRecognised.get(i));

            // WEATHER QUERIES IN ALL LOCATIONS  TODO : modify preference file in order to get locations
            if (speechRecognised.get(i).contains("cardview_weather_city") & speechRecognised.get(i).contains("in") & speechRecognised.get(i).contains("locations")) {

                if(speechRecognised.get(i).contains("tomorrow")) {
                    fragmentDisplayed = "location";
                    speechReplayed = "Weather in your locations for tomorrow are these";
                    isSpeechRecognised = true;
                    break;
                }

                else if(speechRecognised.get(i).contains("forecast")) {
                    fragmentDisplayed = "location";
                    speechReplayed = "This is the forecast cardview_weather_city in your locations";
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "location";
                    speechReplayed = "Weather in your locations are these";
                    isSpeechRecognised = true;
                    break;
                }

            }


            // WEATHER QUERIES IN CERTAIN LOCATION
            else if (speechRecognised.get(i).contains("cardview_weather_city") & speechRecognised.get(i).contains("in")) {

                if(speechRecognised.get(i).contains("tomorrow")) {
                    fragmentDisplayed = "location";
                    cityRequest = getLocationFromRequest(speechRecognised.get(i), "in");
                    speechReplayed = "Weather in " + cityRequest +  " for tomorrow is sunny";
                    isSpeechRecognised = true;
                    break;
                }

                else if(speechRecognised.get(i).contains("forecast")) {
                    fragmentDisplayed = "location";
                    cityRequest = getLocationFromRequest(speechRecognised.get(i), "in");
                    speechReplayed = "This is the forecast cardview_weather_city in " + cityRequest;
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "location";
                    cityRequest = getLocationFromRequest(speechRecognised.get(i), "in");
                    speechReplayed = "Weather in " + cityRequest + "is sunny";
                    isSpeechRecognised = true;
                    break;
                }

            }


            // WEATHER QUERIES IN DEFAULT LOCATION TODO : modify preference file in order to get default location
            else if (speechRecognised.get(i).contains("cardview_weather_city")) {

                if(speechRecognised.get(i).contains("tomorrow")) {
                    fragmentDisplayed = "home";
                    speechReplayed = "Weather in your default location for tomorrow is sunny";
                    isSpeechRecognised = true;
                    break;
                }

                else if(speechRecognised.get(i).contains("forecast")) {
                    fragmentDisplayed = "home";
                    speechReplayed = "This is the forecast cardview_weather_city in your default location";
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "home";
                    speechReplayed = "Weather in your default location is sunny";
                    isSpeechRecognised = true;
                    break;
                }

            }


            // DEFAULT LOCATION QUERIES TODO : modify preference file in order to know default location
            else if (speechRecognised.get(i).contains("location")) {
                if ((speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")) && speechRecognised.get(i).contains("default")) {
                    fragmentDisplayed = "user";
                    speechReplayed = "This is your default location";
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
                    speechReplayed = "Your default location was changed to" + cityRequest;
                    isSpeechRecognised = true;
                    break;
                }

                else {
                    fragmentDisplayed = "voice";
                    speechReplayed = "Sorry, I didn't understand you. Could you please repeat the question?";
                    isSpeechRecognised = false;
                    continue;
                }

            }

            // TODO: Get location from GPS and get the city or town
            else if (speechRecognised.get(i).contains("where")) {
                fragmentDisplayed = "location";
                locationManagerService.getMyLastCoordinates();
                String myPlace = locationManagerService.getMyPlace();
                speechReplayed = "You are in " + myPlace;
                isSpeechRecognised = true;

            }



            // CALENDAR QUERIES

            else if (speechRecognised.get(i).contains("event") || speechRecognised.get(i).contains("events") || speechRecognised.get(i).contains("calendar")) {

                if (speechRecognised.get(i).contains("tell") || speechRecognised.get(i).contains("give") || speechRecognised.get(i).contains("check")) {

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
                    fragmentDisplayed = "voice";
                    speechReplayed = "Sorry, I didn't understand you. Could you please repeat the question?";
                    isSpeechRecognised = false;
                    continue;
                }

            }

            // UNKNOWN QUERY

            else {

                fragmentDisplayed = "voice";
                speechReplayed = "Sorry, I didn't understand you. Could you please repeat the question?";
                isSpeechRecognised = false;
                continue;
            }

        }

        System.out.println(speechReplayed);
        System.out.println(fragmentDisplayed);


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


    public ArrayList<String> getSpeechRecognised() {
        return speechRecognised;
    }

    public void setSpeechRecognised(ArrayList<String> speechRecognised) {
        this.speechRecognised = speechRecognised;
    }

}
