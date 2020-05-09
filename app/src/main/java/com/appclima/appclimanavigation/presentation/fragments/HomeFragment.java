package com.appclima.appclimanavigation.presentation.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherCityCard;
import com.appclima.appclimanavigation.utilities.Font_icons;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    RecyclerView recyclerWeatherCards;
    ArrayList<Cities> cityList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        // city contained in recyclerview:
        cityList = new ArrayList<>();

        // Define linear layout manager:
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // find recycler view in home fragment by ID
        recyclerWeatherCards = (RecyclerView) homeView.findViewById(R.id.weather_cities_RV);

        System.out.println(getContext());
        // We need to use getContext because we are not in MainActivity but in fragment:
        recyclerWeatherCards.setLayoutManager(layoutManager);

        // When the fragment is created, look in preference files which cities should appear and create them:
        getListCities();


        WeatherCityCard weatherCityCard = new WeatherCityCard(getContext(), cityList);
        recyclerWeatherCards.setAdapter(weatherCityCard);



        /*
        // Define font used for icons:
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.iconFontPath));


        // setTypeface in every textView:
        TextView current_day_icon = (TextView) root.findViewById(R.id.current_weather_icon_home);
        current_day_icon.setTypeface(font);

        TextView first_day_icon_forecast = (TextView) root.findViewById(R.id.day1_weather_icon_home);
        first_day_icon_forecast.setTypeface(font);

        TextView second_day_icon_forecast = (TextView) root.findViewById(R.id.day2_weather_icon_home);
        second_day_icon_forecast.setTypeface(font);

        TextView third_day_icon_forecast = (TextView) root.findViewById(R.id.day3_weather_icon_home);
        third_day_icon_forecast.setTypeface(font);

        TextView fourth_day_icon_forecast = (TextView) root.findViewById(R.id.day4_weather_icon_home);
        fourth_day_icon_forecast.setTypeface(font);

        TextView fifth_day_icon_forecast = (TextView) root.findViewById(R.id.day5_weather_icon_home);
        fifth_day_icon_forecast.setTypeface(font);


         */


        // List view
        return homeView;
    }

    private void getListCities() {

        //TODO: Aquí en realidad se deberá de leer el fichero de preferencias para saber las ciudades y llamar a la API para leer los datos:
        cityList.add(new Cities("Barcelona", "Spain", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
        cityList.add(new Cities("Madrid", "Spain", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
        cityList.add(new Cities("Rome", "Italy", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
        cityList.add(new Cities("London", "UK", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
        cityList.add(new Cities("New York", "USA", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
        cityList.add(new Cities("Paris", "France", 23.3, 29.3,21.3,"@string/wi_day_sunny"));
    }
}

