package com.appclima.appclimanavigation.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.APIWeather;
import com.appclima.appclimanavigation.control.ManageCalendar;
import com.appclima.appclimanavigation.control.ManageLocation;
import com.appclima.appclimanavigation.control.ManagePermissions;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.control.SpeechRecognition;
import com.appclima.appclimanavigation.control.VoiceCommands;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.model.Cities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Voice recognition objects
    private SpeechRecognition speechRecognition;
    private VoiceCommands voiceCommands;

    //API Weather
    private APIWeather apiWeather;

    // Location service
    private ManageLocation locationService;
    private FusedLocationProviderClient myFusedLocationClient;

    // Required permissions and request codes defined:
    private ManagePermissions permissionRequestManager;
    public static final String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String WRITE_CALENDAR_PERMISSION = Manifest.permission.WRITE_CALENDAR;
    public static final String READ_CALENDAR_PERMISSION = Manifest.permission.READ_CALENDAR;
    public static final String INTERNET_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final String INTERNET_PERMISSION = Manifest.permission.INTERNET;

    public static final int AUDIO_PERMISSION_REQUEST_CODE = 1;
    public static final int LOCATION_FINE_PERMISSION_REQUEST_CODE = 2;
    public static final int LOCATION_COARSE_PERMISSION_REQUEST_CODE = 3;
    public static final int WRITE_CALENDAR_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_CALENDAR_PERMISSION_REQUEST_CODE = 5;
    public static final int INTERNET_NETWORK_STATE_REQUEST_CODE = 6;
    public static final int INTERNET_PERMISSION_REQUEST_CODE = 7;

    // Manage calendar
    private boolean allowRefresh;


    // Model Array data:
    public ArrayList<CalendarEvents> calendarEventsArray;
    public ArrayList<Chat> voiceMessages;


    // ACTIVITY CYCLE LIFE METHODS:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide title bar
        getSupportActionBar().hide();
        allowRefresh = false;

        // Set activity main view
        setContentView(R.layout.activity_main);

        // Set navigation app bar
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_weather, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Check if permissions are granted:
        checkPermissions();

        // Initialize main services:
        initializeLocation(); // location service
        speechRecognition = new SpeechRecognition(this, this); // voice recognition service
        voiceCommands = new VoiceCommands(this, this, locationService); // voice commands and answers


        // INITIALIZE ARRAY MODELS AND FIRST INFORMATION SHOWED:
        // Initialize CHAT:
        voiceMessages = new ArrayList<>();
        Chat message1 = new Chat("Welcome to your voice assistant, I'm here to help you", 0);
        voiceMessages.add(message1);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionRequestManager.requestPermissionResponse(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // ActivityResult is treated on SpeechRecognition class:
        speechRecognition.HandlerOnActivityResult(requestCode, resultCode, data);
        if(speechRecognition.isWellRecognised()) {
            addChatMessage(speechRecognition.getTextRecognised(), 1);
            addChatMessage(speechRecognition.getTextReplayed(), 0);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // HOME FRAGMENT:
        // NavHostFragment homeFragmentNav = (NavHostFragment) getSupportFragmentManager().findFragmentByTag("Home");
        // HomeFragment homeFragment = (HomeFragment) homeFragmentNav.getChildFragmentManager().findFragmentByTag("Home");

    }

    public String getCoordinates(){
        locationService.getMyLastCoordinates();
        return (locationService.getMyLatitude() + " ," + locationService.getMyLongitude());
    }

    public void restartLocationUpdates () {
        ManagePreferences managePreferences = new ManagePreferences(this);
        if (managePreferences.getLocationUpdates().contains("true")){
            locationService.restartLocationUpdates();

        }
    }

    public void pauseLocationUpdates() {
        ManagePreferences managePreferences = new ManagePreferences(this);
        if (managePreferences.getLocationUpdates().contains("true")){
            locationService.pauseLocationUpdates();

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!allowRefresh){
            System.out.println("Fragment udpate " + allowRefresh);
            allowRefresh = true;
        }
        pauseLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLocationUpdates();
    }

    public void addCityInformation(Cities cityObject) {
        // TODO
    }

    public void addEventCalendar(CalendarEvents eventObject) {
        // TODO
    }


    public void addChatMessage(String textMessage, Integer transmitter) {
        // Adding new message

        Log.d("New message added: ", textMessage);
        voiceMessages.add(new Chat(textMessage, transmitter));
    }

    // Method to initialize Speech Recognition when microphone is clicked
    public void getInputSpeech(View view) {
        System.out.println("Click in input speech");

        speechRecognition.speechRequest(view, voiceCommands);

    }

    // Method to check all permissions needed
    private void checkPermissions() {
        // Check permissions
        permissionRequestManager = new ManagePermissions(this, this);

        // Request permissions:
        permissionRequestManager.permissionManager(LOCATION_PERMISSION, LOCATION_FINE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(LOCATION_COARSE_PERMISSION, LOCATION_COARSE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(AUDIO_PERMISSION, AUDIO_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(WRITE_CALENDAR_PERMISSION, WRITE_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(READ_CALENDAR_PERMISSION, READ_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(INTERNET_NETWORK_STATE, INTERNET_NETWORK_STATE_REQUEST_CODE);
        permissionRequestManager.permissionManager(INTERNET_PERMISSION, INTERNET_PERMISSION_REQUEST_CODE);


    }

    // Method to initialize location updates:
    public void initializeLocation(){
        // Creates object to access to the location:
        myFusedLocationClient = new FusedLocationProviderClient(this);
        myFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println(myFusedLocationClient);
        locationService = new ManageLocation(this, this, myFusedLocationClient);

        // Get last location coordinates in order to check GPS and get last location updated
        locationService.getMyLastCoordinates();
        System.out.println(locationService.getMyLatitude() + "  " + locationService.getMyLongitude());

        // Enable LocationUpdates if are enabled in user preferences
        locationService.setLocationUpdates();
        locationService.locationSettings();
    }



    // Getters and setters:
    public SpeechRecognition getSpeechRecognition() {
        return speechRecognition;
    }

    public void setSpeechRecognition(SpeechRecognition speechRecognition) {
        this.speechRecognition = speechRecognition;
    }

    public ManageLocation getLocationService() {
        return locationService;
    }

    public void setLocationService(ManageLocation locationService) {
        this.locationService = locationService;
    }

    public FusedLocationProviderClient getMyFusedLocationClient() {
        return myFusedLocationClient;
    }

    public void setMyFusedLocationClient(FusedLocationProviderClient myFusedLocationClient) {
        this.myFusedLocationClient = myFusedLocationClient;
    }

    public ManagePermissions getPermissionRequestManager() {
        return permissionRequestManager;
    }

    public void setPermissionRequestManager(ManagePermissions permissionRequest) {
        this.permissionRequestManager = permissionRequest;
    }



    public ArrayList<CalendarEvents> getCalendarEventsArray() {
        return calendarEventsArray;
    }

    public void setCalendarEventsArray(ArrayList<CalendarEvents> calendarEventsArray) {
        this.calendarEventsArray = calendarEventsArray;
    }

    public ArrayList<Chat> getVoiceMessagesArray() {
        return voiceMessages;
    }

    public void setVoiceMessages(ArrayList<Chat> voiceMessages) {
        this.voiceMessages = voiceMessages;
    }


    public boolean isAllowRefresh() {
        return allowRefresh;
    }

    public void setAllowRefresh(boolean allowRefresh) {
        this.allowRefresh = allowRefresh;
    }
}




