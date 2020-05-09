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

    // Manage permissions
    public void permissionManager() {
        checkPermissionEnabled();
        System.out.println(permissionRequest + " is " + permissionEnabled);

        if (permissionEnabled == false){
            enablesPermissionRequest();
        }

    }

    // Check permissions and returns if it's already enabled or not
    public void checkPermissionEnabled() {

        if (ActivityCompat.checkSelfPermission(myContext, permissionRequest) == PackageManager.PERMISSION_GRANTED) {
            permissionEnabled = true;
            System.out.println(permissionRequest + "is granted");
        }

        else {

            permissionEnabled = false;
            System.out.println(permissionRequest + "is NOT granted");

        }

    }


    // Request Permissions in case is not enabled:
    private void enablesPermissionRequest() {
        System.out.println("Request permission " + permissionRequest);

        switch (permissionRequest) {
            case Manifest.permission.RECORD_AUDIO:
                requestCodePermission = 1;
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                requestCodePermission = 101;
                break;

            case Manifest.permission.ACCESS_COARSE_LOCATION:
                requestCodePermission = 2;
                break;
            default:
                requestCodePermission = 21;
                break;
        }

        ActivityCompat.requestPermissions(myActivity, new String[]{permissionRequest},requestCodePermission);
    }


    public void handleOnRequestPermissionResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println(PackageManager.PERMISSION_GRANTED);
        switch (requestCode){
            case 101:
                System.out.println("Location request");
                if (ContextCompat.checkSelfPermission(myContext, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location fine granted", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(myContext, "Location fine denied", Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                System.out.println("Audio request");
                if (ContextCompat.checkSelfPermission(myContext, AUDIO_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Audio granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Audio denied", Toast.LENGTH_LONG).show();
                }
                break;

            case 10:
                System.out.println("Audio request");
                if (ContextCompat.checkSelfPermission(myContext, LOCATION_COARSE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(myContext, "Location coarse granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(myContext, "Location coarse denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


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
