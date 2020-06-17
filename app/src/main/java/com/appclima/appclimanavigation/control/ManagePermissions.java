package com.appclima.appclimanavigation.control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.utilities.PermissionsConstants;


public class ManagePermissions implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Required permissions and request codes defined:
    private Activity myActivity;
    private boolean permissionEnabled = false;
    private Context myContext;



    public ManagePermissions(Activity activity, Context context) {
        this.myActivity = activity;
        this.myContext = context;
    }


    public void requestCalendarPermissions() {

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR}, PermissionsConstants.READ_CALENDAR_PERMISSION_REQUEST_CODE);
        } else if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
        }

    }

    public void requestLocationPermissions() {

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionsConstants.LOCATION_FINE_PERMISSION_REQUEST_CODE);

        } else if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }

    }


    public void requestInternetPermission() {

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE}, PermissionsConstants.INTERNET_PERMISSION_REQUEST_CODE);

        } else if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
        }

    }

    public void requestAudioPermission() {

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.RECORD_AUDIO}, PermissionsConstants.AUDIO_PERMISSION_REQUEST_CODE);

        } else if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        }

    }



    // GETTTERS AND SETTERS:

    public boolean isPermissionEnabled(String permission) {
        if (ActivityCompat.checkSelfPermission(myContext, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {

            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionsConstants.LOCATION_FINE_PERMISSION_REQUEST_CODE:
                System.out.println("Location fine request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location fine granted", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(myContext, "Location fine denied", Toast.LENGTH_LONG).show();
                }
                break;
            case PermissionsConstants.AUDIO_PERMISSION_REQUEST_CODE:
                System.out.println("Audio request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Audio granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Audio denied", Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionsConstants.LOCATION_COARSE_PERMISSION_REQUEST_CODE:
                System.out.println("Location coarse request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location coarse granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Location coarse denied", Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionsConstants.READ_CALENDAR_PERMISSION_REQUEST_CODE:
                System.out.println("Read calendar request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Calendar read granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Calendar read denied", Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionsConstants.WRITE_CALENDAR_PERMISSION_REQUEST_CODE:
                System.out.println("Write calendar request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Calendar write granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Calendar write denied", Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionsConstants.INTERNET_NETWORK_STATE_REQUEST_CODE:
                System.out.println("Network request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Network state granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Network state denied", Toast.LENGTH_LONG).show();
                }
                break;

            case PermissionsConstants.INTERNET_PERMISSION_REQUEST_CODE:
                System.out.println("Network request");
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Network state granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Network state denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
