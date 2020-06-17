package com.appclima.appclimanavigation.control;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;


public class ManageLocation extends MainActivity {

    private Context myContext;
    private Activity myActivity;
    private Location myLocation;
    private String myPlace;
    private double myLatitude;
    private double myLongitude;
    private LocationRequest myLocationRequest;
    private LocationCallback myLocationCallback;
    private LocationManager myManager;
    private Geocoder myGeocoder;
    private FusedLocationProviderClient myFusedLocationClient;
    private String cityPlace;



    // Constructor for MainActivity onCreate:
    public ManageLocation(Context myContext, Activity myActivity, FusedLocationProviderClient myFusedLocation) {
        this.myContext = myContext;
        this.myActivity = myActivity;
        this.myFusedLocationClient = myFusedLocation;
        System.out.println("Location created");
    }




    public void pauseLocationUpdates() {
        // stop location updates
        myFusedLocationClient.removeLocationUpdates(myLocationCallback);
    }

    public void restartLocationUpdates() {
        // restart location updates
        setLocationUpdates();
    }


    //
    public boolean isLocationUpdateEnabled () {
        ManagePreferences managePreferences = new ManagePreferences(myContext);
        String locationUpdatesEnabled = managePreferences.getLocationUpdates();

        if (locationUpdatesEnabled.contains("true")) {
            System.out.println("Location update enabled");
            return true;
        }

        else {
            System.out.println("Location update disabled");
            return false;
        }
    }


    public String getMyLastLocation (double latitude, double longitude) {

        myPlace = null;
        myGeocoder = new Geocoder(myActivity.getApplicationContext());
        List<Address> nameLocation = null;

        try {
            // Get place from coordinates:
            nameLocation = myGeocoder.getFromLocation(latitude, longitude, 1);
            // String address = nameLocation.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            cityPlace = nameLocation.get(0).getLocality();
            String state = nameLocation.get(0).getAdminArea();
            String country = nameLocation.get(0).getCountryName();


            StringBuilder placeString = new StringBuilder();
            String strLocation [] = {cityPlace, " , ", state, " , ", country};

            for (String temp: strLocation){
                // Append only when the string is not null
                if (temp != null)
                {
                    placeString.append(temp);
                }
            }

            // Get string from place:
            return placeString.toString();
        }
        catch(IOException ex) {
            return null;
        }

    }


    // Request my last location
    public void getMyLastCoordinates () {
        System.out.println("Get my last location");

        System.out.println("Checking GPS...");
        checkGPS();

        // Access to the client and look for the last location saved:
        System.out.println(myFusedLocationClient.getLastLocation());
        myFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            // In case last location exists:
            public void onSuccess(Location location) {
                // If location is not null, update location
                if (location != null) {
                    //TODO: Show Location and translate it to a city

                    // Update my location atributes:
                    myLocation = location;
                    myLatitude = location.getLatitude();
                    myLongitude = location.getLongitude();

                    myPlace = getMyLastLocation(myLatitude, myLongitude);
                }

                else {
                    //If we have no location, we create a request:
                    System.out.println("location is null, we try to request location: ");
                    checkGPS();
                    // Creates location request:
                    setLocationUpdates();
                }

            }
        });
    }




    // The device's system settings may be set up:
    public void locationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(myLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(myContext).checkLocationSettings(builder.build());

    }


    public void setLocationUpdates () {

        if (isLocationUpdateEnabled()) {
            // Creates and configure location request:
            myLocationRequest = LocationRequest.create();
            myLocationRequest.setInterval(100000);
            myLocationRequest.setFastestInterval(50000);
            myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            myLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        System.out.println("Can't request location");
                        // Set up device settings for location request:
                        locationSettings();
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.
                            myLocation = location;
                            myLatitude = location.getLatitude();
                            myLongitude = location.getLongitude();
                            System.out.println("Location updated to " + myLatitude + ", " +  myLongitude);

                            myPlace = getMyLastLocation(myLatitude, myLongitude);
                            System.out.println(cityPlace);
                            ManagePreferences managePreferences = new ManagePreferences(myContext);
                            managePreferences.changeLocation(cityPlace, 1);


                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(myContext).requestLocationUpdates(myLocationRequest, myLocationCallback, null);
        }

    }

    private void checkGPS(){
        myManager =  (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);
        System.out.println(myManager);

        // In case GPS is not enabled:
        if ( !myManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps(); // Ask for permission to enable it
        }

    }

    private void buildAlertMessageNoGps() {

        AlertDialog.Builder alertGPS = new AlertDialog.Builder(myContext, android.R.style.Theme_Material_Dialog_Alert);
        alertGPS.setMessage("GPS is disabled, do you want to enable it?");
        alertGPS.setCancelable(false);

        alertGPS.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.cancel();
                    }
                });

        alertGPS.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertGPS.create();
        alert11.show();
    }

    public String getMyPlace() {
        return myPlace;
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

}

