package com.appclima.appclimanavigation.presentation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.cardviews.CalendarEventCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherCityCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherScreenCityCard;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class WeatherFragment extends Fragment {

    RecyclerView recyclerWeatherCard;
    ArrayList<Cities> cityList;
    ArrayList<ForecastCity> cityForecastList;
    MainActivity myActivity;


    // Empty constructor:
    public WeatherFragment() {
        // When the fragment is created, look in which information should be appear:
        // getFirstInformation();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Weather Fragment", "onCreate Method");
        // this fragments is part of main activity
        myActivity = (MainActivity) getActivity();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

    // Set the fragment view
    final View weatherView = inflater.inflate(R.layout.fragment_weather, container, false);

        initializeRVWeatherScreen(weatherView);

        return weatherView;
    }

    private void initializeRVWeatherScreen (View myView) {


        getCardInformation();


        if (cityList != null) {

            // RECYCLERVIEW CITIES:
            Log.d("Weather Fragment View", "initializing view elements...");

            // Define linear layout manager:
            LinearLayoutManager layoutManagerWeather = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            // find recycler view in home fragment by ID
            recyclerWeatherCard = (RecyclerView) myView.findViewById(R.id.weather_screen_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerWeatherCard.setLayoutManager(layoutManagerWeather);

            // Adapt information to cardview
            WeatherScreenCityCard weatherScreenCityCard = new WeatherScreenCityCard(getContext(), cityList, cityForecastList);

            recyclerWeatherCard.setAdapter(weatherScreenCityCard);

            // Set Indicator to the scroll horizontal view:
            ScrollingPagerIndicator recyclerIndicator = myView.findViewById(R.id.indicator_weather_screen_cities_RV);
            recyclerIndicator.attachToRecyclerView(recyclerWeatherCard);

        }
    }



    private void getCardInformation() {
        Log.d("Weather Fragment: ", "Getting information...");
        cityList = myActivity.getCityListArray();
        cityForecastList = myActivity.getCityForecastListArray();
    }

}