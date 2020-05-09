package com.appclima.appclimanavigation.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManageLocation;
import com.appclima.appclimanavigation.control.ManagePermissions;
import com.appclima.appclimanavigation.control.SpeechRecognition;
import com.appclima.appclimanavigation.control.VoiceCommands;
import com.appclima.appclimanavigation.utilities.Font_icons;
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

public class MainActivity extends AppCompatActivity {

    // Declare fragments as constants to be used in other classes
    // public final HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_home);
    // public final UserFragment userFragment = (UserFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_user);
    // public final CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_calendar);
    // public final LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_location);

    // Importa la clase de iconos (como fuentes)
    Font_icons font_icons;

    // Voice recognition objects
    private SpeechRecognition speechRecognition;
    private VoiceCommands voiceCommands;

    // Location service
    private ManageLocation locationService;
    private FusedLocationProviderClient myFusedLocationClient;
    private boolean userPreferencesLocationUpdateEnabled = true; // TODO: Leer fichero de preferencias para saber si las tiene habilitadas


    // Required permissions
    private ManagePermissions permissionRequestManager;
    public static final String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;


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
        permissionRequestManager.setPermissionRequest(LOCATION_PERMISSION);
        permissionRequestManager.permissionManager();

        permissionRequestManager.setPermissionRequest(LOCATION_COARSE_PERMISSION);
        permissionRequestManager.permissionManager();

        // Set permission treated to audio:
        permissionRequestManager.setPermissionRequest(AUDIO_PERMISSION);
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

        // Icons importation:




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionRequestManager.handleOnRequestPermissionResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // ActivityResult is treated on SpeechRecognition class:
        speechRecognition.HandlerOnActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationService.pauseLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationService.restartLocationUpdates();
    }


    // Inicialize Speech Recognition when microphone is clicked
    public void getInputSpeech(View view) {
        System.out.println("Click in input speech");
        // VoiceFragment voiceFragment = (VoiceFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_voice);
        // voiceFragment.editTextRecognisedSpeech("hola holaaa");
        speechRecognition.speechRequest(view, voiceCommands);
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
}



