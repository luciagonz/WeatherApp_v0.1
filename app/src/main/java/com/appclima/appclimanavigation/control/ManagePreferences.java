package com.appclima.appclimanavigation.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.fragments.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManagePreferences {


    /* Information saved:
        - User information (name)
        - Array with cities names
        - Array with cities type (2: default, 3: favourite)
        - Events?
        - Login in the first activity?
     */

    // Atributes:
    private String namePreferenceFile;
    private String preferenceID;
    private String preferenceValue;
    private int accessModeID;
    private Context myContext;
    SharedPreferences myPreferences;



    // Constructor (all atributes required)
    public ManagePreferences(Context myContext) {
        this.myContext = myContext;
    }

    // Save preferences:
    public void savePreferences(String namePreferenceFile, String preferenceID, String preferenceValue, int accessModeID) {

        // Set access mode according to the ID:

        int accessMode;

        switch (accessModeID) {
            case 0:
                accessMode = myContext.MODE_MULTI_PROCESS;
                break;

            case 1:
                accessMode = myContext.MODE_WORLD_WRITEABLE;
                break;

            case 2:
                accessMode = myContext.MODE_WORLD_READABLE;
                break;

            default:
                accessMode = myContext.MODE_PRIVATE;
                break;

        }

        // As input atribute is String, it's not necessary to parse data.

        // Access to the file:
        myPreferences =  myContext.getSharedPreferences(namePreferenceFile, myContext.MODE_PRIVATE);

        // Create editor:
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString(preferenceID, preferenceValue);
        editor.commit();
    }

    public String getPreferences(String namePreferenceFile, String preferenceID) {

        // As input atribute is String, it's not necessary to parse data.

        // Access to the file:
        myPreferences =  myContext.getSharedPreferences(namePreferenceFile, myContext.MODE_PRIVATE);

        // Returns null if there is no data saved:
        String valueSaved = myPreferences.getString(preferenceID, null);

        return valueSaved;
    }


    // Getters and setters (manually generated)

    public String getTomorrowDateFlag() {

        return getPreferences("UserPrefs", "tomorrowDate");
    }

    public void setTomorrowDateFlag(String tomorrowDate) {
        savePreferences("UserPrefs", "tomorrowDate", tomorrowDate, 3);
    }

    public String getDefaultUnitTemperature() {

        return getPreferences("UserPrefs", "temperatureUnit");
    }

    public void setDefaultUnitTemperature(String temperatureUnit) {
        savePreferences("UserPrefs", "temperatureUnit", temperatureUnit, 3);
    }


    public String getDefaultUnitWind() {

        return getPreferences("UserPrefs", "windUnits");
    }

    public void setDefaultUnitWind(String windUnit) {
        savePreferences("UserPrefs", "windUnits", windUnit, 3);
    }

    public String getUserName() {

        return getPreferences("UserPrefs", "username");
    }

    public void setUserName(String userName) {
        savePreferences("UserPrefs", "username", userName, 3);
    }

    public String getDarkModeTheme(){
        return getPreferences("UserPrefs", "darkMode");
    }

    public void setDarkModeTheme(Boolean darkMode) {
        savePreferences("UserPrefs", "darkMode", String.valueOf(darkMode), 3);
    }

    public String getUmbrellaReminder(){
        return getPreferences("UserPrefs", "umbrellaReminder");
    }

    public void setUmbrellaReminder(Boolean umbrellaReminder) {
        savePreferences("UserPrefs", "umbrellaReminder", String.valueOf(umbrellaReminder), 3);
    }

    public String getLocationUpdates(){
        return getPreferences("UserPrefs", "locationUpdates");
    }

    public void setLocationUpdates(Boolean locationUpdates) {
        savePreferences("UserPrefs", "locationUpdates", String.valueOf(locationUpdates), 3);
    }

    public String getDayOfWeek() {
        return getPreferences("UserPrefs", "calendarStartDay");
    }

    public void setDayOfWeek(String dayOfWeek) {
        savePreferences("UserPrefs", "calendarStartDay", dayOfWeek, 3);
    }

    public String getDefaultLocation () {
        System.out.println("Getting shared preferences cities...");
        String citiesNames = getPreferences("UserPrefs","citiesNames");
        List<String> cityNames;
        cityNames = Arrays.asList(citiesNames.split(","));
        System.out.println(cityNames);

        String citiesTypes = getPreferences("UserPrefs","citiesTypes");

        List<String> cityTypes;
        cityTypes = Arrays.asList(citiesTypes.split(","));
        System.out.println(cityTypes);

        String defaultLocation = "";

        for(int i = 0; i < cityTypes.size(); i++) {

            if (cityTypes.get(i).equals("2")) {
                defaultLocation = cityNames.get(i);
                System.out.println("Default location: " + defaultLocation);
            }

        }
        return defaultLocation;
    }

    public void setManagerLayoutPosition(int position) {
        savePreferences("UserPrefs", "managerLayoutPosition", String.valueOf(position), 3);
    }

    public int getManagerLayoutPosition(){
        return Integer.valueOf(getPreferences("UserPrefs", "managerLayoutPosition"));
    }

    public void saveDefaultPreferences (String name, String defaultCity) {

        String onBoarding = "";
        onBoarding = getPreferences("UserPrefs", "onBoardingDone");
        if(onBoarding == null) {

            // TODO: Initialize onBoarding
            setManagerLayoutPosition(0);
            setUmbrellaReminder(false);
            setLocationUpdates(true);
            setDarkModeTheme(false);
            setDayOfWeek("Sunday");
            setDefaultUnitWind("m/s");
            setUserName(name);
            setDefaultUnitTemperature("C");
            setTomorrowDateFlag("false");
            savePreferences("UserPrefs", "citiesNames", defaultCity,3);
            savePreferences("UserPrefs", "citiesTypes", "2",3);
            savePreferences("UserPrefs", "onBoardingDone", "true",3);

        }

    }

    public void removeLocation(int cityPosition) {

        System.out.println("Remove city " + String.valueOf(cityPosition));
        String citiesTypes = getPreferences("UserPrefs","citiesTypes");
        String citiesNames = getPreferences("UserPrefs","citiesNames");

        // City names and types to array:
        String[] commaSeparatedCityArr = citiesNames.split("\\s*,\\s*");
        List<String> cityNameArray = new ArrayList<String>(Arrays.asList(commaSeparatedCityArr));
        System.out.println("Cities: " + cityNameArray);
        cityNameArray.remove(cityPosition);
        System.out.println("New cities: " + cityNameArray);


        // City names and types to array:
        String[] commaSeparatedTypesArr = citiesTypes.split("\\s*,\\s*");
        List<String> cityNameTypes = new ArrayList<String>(Arrays.asList(commaSeparatedTypesArr));
        System.out.println("Types: " + cityNameTypes);
        cityNameTypes.remove(cityPosition);
        System.out.println("New types: " + cityNameTypes);


        StringBuilder csvBuilderCity = new StringBuilder();
        StringBuilder csvBuilderType = new StringBuilder();


        for(String city : cityNameArray){
            csvBuilderCity.append(city);
            csvBuilderCity.append(",");
        }

        for (String type : cityNameTypes) {
            csvBuilderType.append(type);
            csvBuilderType.append(",");
        }

        String newCityList = csvBuilderCity.toString();
        String newCityTypes = csvBuilderType.toString();

        if (newCityList.length() > 1) {

            newCityList = newCityList.substring(0, newCityList.length() - (",").length());
            newCityTypes = newCityTypes.substring(0, newCityTypes.length() - (",").length());

            System.out.println(newCityList);
            System.out.println(newCityTypes);

        }

        savePreferences("UserPrefs", "citiesTypes", newCityTypes, 3);
        savePreferences("UserPrefs", "citiesNames", newCityList, 3);

    }


    public int getCityPosition(String city) {
        final String citiesTypes = getPreferences("UserPrefs","citiesTypes");
        final String citiesNames = getPreferences("UserPrefs","citiesNames");

        // City names and types to array:
        String[] commaSeparatedCityArr = citiesNames.split("\\s*,\\s*");
        List<String> cityNameArray = new ArrayList<String>(Arrays.asList(commaSeparatedCityArr));
        System.out.println("Cities: " + cityNameArray);
        System.out.println("There are " +  cityNameArray.size() + " cities: " + cityNameArray);

        int position = -1;

        for (int i = 0; i < cityNameArray.size(); i++) {
            System.out.println("Evaluated city :" + cityNameArray.get(i));
            if (cityNameArray.get(i).contains(city)) {
                position = i;
            }
        }

        return position;
    }

    public void changeLocation(final String cityAdded, Integer cityAddedType){
        final String citiesTypes = getPreferences("UserPrefs","citiesTypes");
        final String citiesNames = getPreferences("UserPrefs","citiesNames");

        // City names and types to array:
        String[] commaSeparatedCityArr = citiesNames.split("\\s*,\\s*");
        List<String> cityNameArray = new ArrayList<String>(Arrays.asList(commaSeparatedCityArr));
        System.out.println("Cities: " + cityNameArray);

        String[] commaSeparatedTypeArr = citiesTypes.split("\\s*,\\s*");
        List<String> cityNameTypes = new ArrayList<String>(Arrays.asList(commaSeparatedTypeArr));
        System.out.println("Types: " + cityNameTypes);

        Log.d("New city", cityAdded);
        cityAdded.replaceAll("\\s", "");


        if (cityAddedType == 2) {
            if (citiesTypes.contains("2")) {
                Log.d("Default location", "Already exists, must be changed.");
                // Default city already defined (must be changed)
                for (int i = 0; i < cityNameTypes.size(); i++) {
                    if (cityNameTypes.get(i).contains("2")) {

                        System.out.println("Default city " + cityNameArray.get(i));
                        cityNameArray.set(i, cityAdded);

                        System.out.println("New city array: " + cityNameArray);

                        StringBuilder csvBuilder = new StringBuilder();

                        for(String city : cityNameArray){
                            csvBuilder.append(city);
                            csvBuilder.append(",");
                        }

                        String newCityList = csvBuilder.toString();
                        newCityList = newCityList.substring(0, newCityList.length() - (",").length());
                        savePreferences("UserPrefs", "citiesNames", newCityList, 3);
                    }
                }
            }
            // Default city not defined (add new city to array)
            else {
                Log.d("Default location", "Doesn't exists, must be added.");
                int position = 0;

                for (int i = 0; i < cityNameTypes.size(); i++) {
                    if (cityNameTypes.get(i).contains("1")) {
                        position = i + 1;
                    }
                }

                cityNameArray.add(position, cityAdded);
                cityNameTypes.add(position, "2");

                System.out.println("New city array: " + cityNameArray);
                System.out.println("New city type: " + cityNameTypes);


                StringBuilder builderCityName = new StringBuilder();
                StringBuilder builderCityType = new StringBuilder();

                for(String city : cityNameArray){
                    builderCityName.append(city);
                    builderCityName.append(",");
                }

                for (String type: cityNameTypes){
                    builderCityType.append(type);
                    builderCityType.append(",");
                }

                String newCityList = builderCityName.toString();
                String newCityTypeList = builderCityType.toString();

                newCityList = newCityList.substring(0, newCityList.length() - (",").length());
                newCityTypeList = newCityTypeList.substring(0, newCityTypeList.length() - (",").length());

                System.out.println(newCityList);
                System.out.println(newCityTypeList);

                savePreferences("UserPrefs", "citiesNames", newCityList, 3);
                savePreferences("UserPrefs", "citiesTypes", newCityTypeList, 3);


            }

        }

        if (cityAddedType == 1) {

            if (citiesTypes.contains("1")) {
                Log.d("GPS location", "Already exists, must be changed.");
                // GPS city already defined (must be changed)
                for (int i = 0; i < cityNameTypes.size(); i++) {
                    if (cityNameTypes.get(i).contains("1")) {
                        System.out.println("GPS city " + cityNameArray.get(i));
                        cityNameArray.set(i, cityAdded);

                        System.out.println("New city array: " + cityNameArray);
                        StringBuilder csvBuilder = new StringBuilder();

                        for (String city : cityNameArray) {
                            csvBuilder.append(city);
                            csvBuilder.append(",");
                        }

                        String newCityList = csvBuilder.toString();
                        newCityList = newCityList.substring(0, newCityList.length() - (",").length());
                        savePreferences("UserPrefs", "citiesNames", newCityList, 3);
                    }
                }

            }

            else {
                Log.d("GPS location", "Doesn't exists, must be added.");


                cityNameArray.add(0, cityAdded);
                cityNameTypes.add(0, "1");

                System.out.println("New city array: " + cityNameArray);
                System.out.println("New city type: " + cityNameTypes);


                StringBuilder builderCityName = new StringBuilder();
                StringBuilder builderCityType = new StringBuilder();

                for (String city : cityNameArray) {
                    builderCityName.append(city);
                    builderCityName.append(",");
                }

                for (String type : cityNameTypes) {
                    builderCityType.append(type);
                    builderCityType.append(",");
                }

                String newCityList = builderCityName.toString();
                String newCityTypeList = builderCityType.toString();

                newCityList = newCityList.substring(0, newCityList.length() - (",").length());
                newCityTypeList = newCityTypeList.substring(0, newCityTypeList.length() - (",").length());

                System.out.println(newCityList);
                System.out.println(newCityTypeList);

                savePreferences("UserPrefs", "citiesNames", newCityList, 3);
                savePreferences("UserPrefs", "citiesTypes", newCityTypeList, 3);


            }
        }

        if (cityAddedType == 3) {
            if (citiesNames.contains(cityAdded)) {
                System.out.println("City is already added");
                AlertDialog dialog = new AlertDialog.Builder(myContext, R.style.MaterialThemeDialog).create();
                dialog.setTitle("Duplicate City");
                dialog.setMessage(cityAdded + " is already added to your cities, do you want to add it again?");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                String newCitiesPrefs = citiesNames + "," + cityAdded;
                                String newCitiesTypesPrefs = citiesTypes + ",3";
                                savePreferences("UserPrefs", "citiesNames", newCitiesPrefs, 3);
                                savePreferences("UserPrefs", "citiesTypes", newCitiesTypesPrefs, 3);
                            }
                        });

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                            }
                        });
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(myContext, R.color.colorPrimaryDark));
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(myContext, R.color.colorPrimaryDark));


            }

            else {
                System.out.println("City added");
                String newCitiesPrefs = citiesNames + "," + cityAdded;
                String newCitiesTypesPrefs = citiesTypes + ",3";
                savePreferences("UserPrefs", "citiesNames", newCitiesPrefs, 3);
                savePreferences("UserPrefs", "citiesTypes", newCitiesTypesPrefs, 3);
            }
        }

    }

}
