package com.appclima.appclimanavigation.control;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appclima.appclimanavigation.presentation.activities.MainActivity;


public class ManagePermissions extends MainActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Activity myActivity;
    private boolean permissionEnabled = false;
    private String permissionRequest;
    private Context myContext;
    private Integer requestCodePermission = null;



    public ManagePermissions(Activity activity, Context context) {
        this.myActivity = activity;
        this.myContext = context;
    }


    // Check if are granted or not, and ask for them if are not:
    public void permissionManager(String permission, int request_code) {

        setPermissionRequest(permission);
        setRequestCodePermission(request_code);

        if (ActivityCompat.checkSelfPermission(myContext, permissionRequest) == PackageManager.PERMISSION_GRANTED) {
            permissionEnabled = true;
            System.out.println(permissionRequest + "is granted");
        }

        else {

            permissionEnabled = false;
            System.out.println(permissionRequest + "is NOT granted");
            System.out.println("Request permission " + permissionRequest);
            ActivityCompat.requestPermissions(myActivity, new String[]{permissionRequest},requestCodePermission);


        }
    }


    public void requestPermissionResponse (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case LOCATION_FINE_PERMISSION_REQUEST_CODE:
                System.out.println("Location fine request");
                if (ContextCompat.checkSelfPermission(myContext, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location fine granted", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(myContext, "Location fine denied", Toast.LENGTH_LONG).show();
                }
                break;
            case AUDIO_PERMISSION_REQUEST_CODE:
                System.out.println("Audio request");
                if (ContextCompat.checkSelfPermission(myContext, AUDIO_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Audio granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Audio denied", Toast.LENGTH_LONG).show();
                }
                break;

            case LOCATION_COARSE_PERMISSION_REQUEST_CODE:
                System.out.println("Location coarse request");
                if (ContextCompat.checkSelfPermission(myContext, LOCATION_COARSE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location coarse granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Location coarse denied", Toast.LENGTH_LONG).show();
                }
                break;

            case READ_CALENDAR_PERMISSION_REQUEST_CODE:
                System.out.println("Read calendar request");
                if (ContextCompat.checkSelfPermission(myContext, READ_CALENDAR_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Calendar read granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Calendar read denied", Toast.LENGTH_LONG).show();
                }
                break;

            case WRITE_CALENDAR_PERMISSION_REQUEST_CODE:
                System.out.println("Write calendar request");
                if (ContextCompat.checkSelfPermission(myContext, WRITE_CALENDAR_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Calendar write granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Calendar write denied", Toast.LENGTH_LONG).show();
                }
                break;

            case INTERNET_NETWORK_STATE_REQUEST_CODE:
                System.out.println("Network request");
                if (ContextCompat.checkSelfPermission(myContext, INTERNET_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Network state granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Network state denied", Toast.LENGTH_LONG).show();
                }
                break;

            case INTERNET_PERMISSION_REQUEST_CODE:
                System.out.println("Internet access request");
                if (ContextCompat.checkSelfPermission(myContext, INTERNET_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Internet access granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Internet access denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    // GETTTERS AND SETTERS:

    public boolean isPermissionEnabled() {
        return permissionEnabled;
    }

    public void setPermissionEnabled(boolean permissionEnabled) {
        this.permissionEnabled = permissionEnabled;
    }

    public Activity getMyActivity() {
        return myActivity;
    }

    public void setMyActivity(Activity myActivity) {
        this.myActivity = myActivity;
    }


    public void setPermissionRequest(String permissionRequest) {
        this.permissionRequest = permissionRequest;
    }

    public String getPermissionRequest() {
        return permissionRequest;
    }

    public Context getMyContext() {
        return myContext;
    }

    public void setMyContext(Context myContext) {
        this.myContext = myContext;
    }

    public Integer getRequestCodePermission() {
        return requestCodePermission;
    }

    public void setRequestCodePermission(Integer requestCodePermission) {
        this.requestCodePermission = requestCodePermission;
    }
}
