package com.appclima.appclimanavigation.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManageLocation;
import com.appclima.appclimanavigation.control.ManagePermissions;
import com.appclima.appclimanavigation.control.SpeechRecognition;
import com.appclima.appclimanavigation.control.VoiceCommands;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.presentation.fragments.VoiceFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    // Voice recognition objects
    private SpeechRecognition speechRecognition;
    private VoiceCommands voiceCommands;

    // Location service
    private ManageLocation locationService;
    private FusedLocationProviderClient myFusedLocationClient;
    private boolean userPreferencesLocationUpdateEnabled = true; // TODO: Leer fichero de preferencias para saber si las tiene habilitadas

    // Required permissions and request codes defined:
    private ManagePermissions permissionRequestManager;
    public static final String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String WRITE_CALENDAR_PERMISSION = Manifest.permission.WRITE_CALENDAR;
    public static final String READ_CALENDAR_PERMISSION = Manifest.permission.READ_CALENDAR;

    public static final int AUDIO_PERMISSION_REQUEST_CODE = 1;
    public static final int LOCATION_FINE_PERMISSION_REQUEST_CODE = 2;
    public static final int LOCATION_COARSE_PERMISSION_REQUEST_CODE = 3;
    public static final int WRITE_CALENDAR_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_CALENDAR_PERMISSION_REQUEST_CODE = 5;


    // Model Array data:
    public ArrayList<Cities> cityListArray;
    public ArrayList<CalendarEvents> calendarEventsArray;
    public ArrayList<Chat> voiceMessages;


    // To provide methods to other classes:
    private static MainActivity instance;


    // Buttons and alarms:

    // Initialize Speech Recognition when microphone is clicked
    public void getInputSpeech(View view) {
        System.out.println("Cantidad de chats antes" + voiceMessages.size());
        System.out.println("Click in input speech");

        speechRecognition.speechRequest(view, voiceCommands);

        System.out.println(voiceCommands.getFragmentDisplayed());

        System.out.println("Cantidad de chats despues" + voiceMessages.size());

    }


    // ACTIVITY CYCLE LIFE METHODS:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide title bar
        getSupportActionBar().hide();

        // Set activity main view
        setContentView(R.layout.activity_main);

        // Set navigation app bar
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_location, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Check permissions
        permissionRequestManager = new ManagePermissions(this, this);

        // Location fine:
        permissionRequestManager.setPermissionRequest(LOCATION_PERMISSION);
        permissionRequestManager.setRequestCodePermission(LOCATION_FINE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager();

        // Location coarse:
        permissionRequestManager.setPermissionRequest(LOCATION_COARSE_PERMISSION);
        permissionRequestManager.setRequestCodePermission(LOCATION_COARSE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager();

        // Audio:
        permissionRequestManager.setPermissionRequest(AUDIO_PERMISSION);
        permissionRequestManager.setRequestCodePermission(AUDIO_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager();

        // Calendar write:
        permissionRequestManager.setPermissionRequest(WRITE_CALENDAR_PERMISSION);
        permissionRequestManager.setRequestCodePermission(WRITE_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager();

        // Calendar write:
        permissionRequestManager.setPermissionRequest(READ_CALENDAR_PERMISSION);
        permissionRequestManager.setRequestCodePermission(READ_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager();


        // Creates object to access to the location:
        myFusedLocationClient = new FusedLocationProviderClient(this);
        myFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println(myFusedLocationClient);
        locationService = new ManageLocation(this, this, myFusedLocationClient);

        // Get last location coordinates in order to check GPS and get last location updated
        locationService.getMyLastCoordinates();

        // Enable LocationUpdates if are enabled in user preferences
        locationService.setEnableLocationUpdates(userPreferencesLocationUpdateEnabled);

        locationService.setLocationUpdates();
        locationService.locationSettings();

        // Creates speech recognition:
        speechRecognition = new SpeechRecognition(this, this);
        voiceCommands = new VoiceCommands(this, this, locationService);


        /* TODO: Pasos para agregar o quitar informacion:
                - On create: archivo de preferencias + localización
                - Llamadas de voz o botones: añadir nuevas al array de elementos.
         */



        // Declare array from models:
        cityListArray = new ArrayList<>();
        calendarEventsArray = new ArrayList<>();
        voiceMessages = new ArrayList<>();

        Chat message1 = new Chat("Welcome to your voice assistant, I'm here to help you", 0);
        voiceMessages.add(message1);


        // TODO 1 STEP: Recover data from preferences storage



        // TODO 2 STEP: Get city from location



        // TODO 3 STEP: Connect with the API in order to get weather information


        // Before implementation:
        Cities city1 = new Cities("Barcelona", "Spain", 23.3, 29.3, 21.3, R.string.wi_cloudy, 1);
        Cities city2 = new Cities("Barcelona", "Spain", 23.3, 29.3, 21.3, R.string.wi_cloudy, 1);
        Cities city3 = new Cities("Barcelona", "Spain", 23.3, 29.3, 21.3, R.string.wi_cloudy, 1);


        cityListArray.add(city1);
        cityListArray.add(city2);
        cityListArray.add(city3);


        CalendarEvents event1 = new CalendarEvents("Comida Juan", "Maremagnum", "12:00", "13:00", "2020/05/14");
        CalendarEvents event2 = new CalendarEvents("Entrega trabajo", "Racó", "19:00", "20:00", "2020/05/14");
        CalendarEvents event3 = new CalendarEvents("Paseo en bici", "Barcelona", "22:00", "23:00", "2020/05/14");


        calendarEventsArray.add(event1);
        calendarEventsArray.add(event2);
        calendarEventsArray.add(event3);

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

    @Override
    protected void onPause() {
        super.onPause();
        // locationService.pauseLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // locationService.restartLocationUpdates();
    }



    public static MainActivity getInstance() {
        return instance;
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

    public ArrayList<Cities> getCityListArray() {
        return cityListArray;
    }

    public void setCityListArray(ArrayList<Cities> cityListArray) {
        this.cityListArray = cityListArray;
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
}



