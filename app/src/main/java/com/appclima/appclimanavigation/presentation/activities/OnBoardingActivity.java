package com.appclima.appclimanavigation.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePermissions;
import com.appclima.appclimanavigation.control.ManagePreferences;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        getSupportActionBar().hide();


        Button enterButton = findViewById(R.id.onBoarding_enter_button);
        final EditText defaultCityOnBoarding = findViewById(R.id.onBoarding_city);
        final EditText defaultName = findViewById(R.id.onBoarding_name);

        checkPermissions();



        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagePreferences managePreferences = new ManagePreferences(getApplicationContext());
                managePreferences.saveDefaultPreferences(defaultName.getText().toString(), defaultCityOnBoarding.getText().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent welcomeIntent = new Intent (OnBoardingActivity.this, MainActivity.class);
                        startActivity(welcomeIntent);
                        finish();
                    }
                }, 1000);
            }
        });
    }

    private void checkPermissions() {
        // Check permissions
        ManagePermissions permissionRequestManager = new ManagePermissions(this, this);

        // Request permissions:
        permissionRequestManager.permissionManager(MainActivity.LOCATION_PERMISSION, MainActivity.LOCATION_FINE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.LOCATION_COARSE_PERMISSION, MainActivity.LOCATION_COARSE_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.AUDIO_PERMISSION, MainActivity.AUDIO_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.WRITE_CALENDAR_PERMISSION, MainActivity.WRITE_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.READ_CALENDAR_PERMISSION, MainActivity.READ_CALENDAR_PERMISSION_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.INTERNET_NETWORK_STATE, MainActivity.INTERNET_NETWORK_STATE_REQUEST_CODE);
        permissionRequestManager.permissionManager(MainActivity.INTERNET_PERMISSION, MainActivity.INTERNET_PERMISSION_REQUEST_CODE);

    }
}
