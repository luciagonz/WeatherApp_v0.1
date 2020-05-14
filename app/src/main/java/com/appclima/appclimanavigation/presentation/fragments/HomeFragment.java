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

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class HomeFragment extends Fragment {

    // https://developer.android.com/guide/components/fragments


    RecyclerView recyclerWeatherCards;
    ArrayList<Cities> cityList;
    ArrayList<ForecastCity> cityForecastList;
    ArrayList<CalendarEvents> calendarEventsList;
    RecyclerView recyclerEventCards;
    MainActivity myActivity;


    // Empty constructor:
    public HomeFragment() {
        // When the fragment is created, look in which information should be appear:
        // getFirstInformation();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Home Fragment", "onCreate Method");
        // this fragments is part of main activity
        myActivity = (MainActivity) getActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Home Fragment View", "onCreateView with " + cityList);

        // Set the fragment to the layout:
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        initializeRVWeather(homeView);

        // List view
        return homeView;
    }

    private void initializeRVWeather (View myView) {


        getCardInformation();


        if(cityList !=null) {

            // RECYCLERVIEW CITIES:
            Log.d("Home Fragment View", "initializing view elements...");

            // Define linear layout manager:
            LinearLayoutManager layoutManagerWeather = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            // find recycler view in home fragment by ID
            recyclerWeatherCards = (RecyclerView) myView.findViewById(R.id.weather_cities_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerWeatherCards.setLayoutManager(layoutManagerWeather);

            // Adapt information to cardview
            WeatherCityCard weatherCityCard = new WeatherCityCard(getContext(), cityList, cityForecastList);

            recyclerWeatherCards.setAdapter(weatherCityCard);

            // Set Indicator to the scroll horizontal view:
            ScrollingPagerIndicator recyclerIndicator = myView.findViewById(R.id.indicator_weather_cities_RV);
            recyclerIndicator.attachToRecyclerView(recyclerWeatherCards);

        }

        if(calendarEventsList !=null) {


            // Define linear layout manager:
            LinearLayoutManager layoutManagerEvents= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            // find recycler view in home fragment by ID
            recyclerEventCards = (RecyclerView) myView.findViewById(R.id.recycler_events_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerEventCards.setLayoutManager(layoutManagerEvents);

            // Adapt information to cardview
            CalendarEventCard calendarEventCard = new CalendarEventCard(getContext(), calendarEventsList);

            recyclerEventCards.setAdapter(calendarEventCard);

            // Set Indicator to the scroll horizontal view:
            // ScrollingPagerIndicator recyclerIndicator = myView.findViewById(R.id.indicator_weather_cities_RV);
            // recyclerIndicator.attachToRecyclerView(recyclerWeatherCards);

        }



    }




    private void getCardInformation() {
        cityList = myActivity.getCityListArray();
        cityForecastList = myActivity.getCityForecastListArray();
        calendarEventsList = myActivity.getCalendarEventsArray();
    }
}


