package com.appclima.appclimanavigation.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePermissions;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.presentation.cardviews.FuncionalityCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherCityCard;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

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

        // RecyclerView data
        ArrayList<String> textFunctionalities = new ArrayList<>();
        ArrayList<Integer> iconsFunctionalities = new ArrayList<>();

        textFunctionalities.add("Get weather forecasts information for selected cities supplied by openweathermap.org");
        textFunctionalities.add("Read, modify and create events in your calendars");
        textFunctionalities.add("And most importantly, you can do it all just using your voice!");

        iconsFunctionalities.add(R.drawable.weather_functionality);
        iconsFunctionalities.add(R.drawable.calendar_functionality);
        iconsFunctionalities.add(R.drawable.voice_functionality);

        // create cards and scrollview
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.onBoarding_rv_functionalities);
        recyclerView.setLayoutManager(layoutManager);
        FuncionalityCard adapter = new FuncionalityCard(this, textFunctionalities, iconsFunctionalities);
        recyclerView.setAdapter(adapter);

        ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.indicator_onBoarding_rv);
        recyclerIndicator.attachToRecyclerView(recyclerView);


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
