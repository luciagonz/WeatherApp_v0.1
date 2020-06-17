package com.appclima.appclimanavigation.presentation.activities;

import androidx.annotation.NonNull;
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

        ManagePermissions managePermissions = new ManagePermissions(this, this);
        managePermissions.requestCalendarPermissions();
        managePermissions.requestAudioPermission();
        managePermissions.requestInternetPermission();
        managePermissions.requestLocationPermissions();



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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
