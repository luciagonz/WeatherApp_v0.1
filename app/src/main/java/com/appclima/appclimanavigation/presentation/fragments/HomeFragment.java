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

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

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

        // We need to use getContext because we are not in MainActivity but in fragment:
        recyclerWeatherCards.setLayoutManager(layoutManager);


        // When the fragment is created, look in preference files which cities should appear and create them:
        getListCities();

        // Adapt information to cardview
        WeatherCityCard weatherCityCard = new WeatherCityCard(getContext(), cityList);

        recyclerWeatherCards.setAdapter(weatherCityCard);

        // Set Indicator to the scroll horizontal view:
        ScrollingPagerIndicator recyclerIndicator = homeView.findViewById(R.id.indicator_weather_cities_RV);
        recyclerIndicator.attachToRecyclerView(recyclerWeatherCards);



        // List view
        return homeView;
    }

    private void getListCities() {

        System.out.println(getContext().getPackageName());
        //TODO: Aquí en realidad se deberá de leer el fichero de preferencias para saber las ciudades y llamar a la API para leer los datos:
        findSymbolbyName("wi_day_sunny");
        cityList.add(new Cities("Barcelona", "Spain", 23.3, 29.3,21.3,findSymbolbyName("wi_sunset"),1));
        cityList.add(new Cities("Madrid", "Spain", 23.3, 29.3,21.3, R.string.wi_cloudy,2));
        cityList.add(new Cities("Rome", "Italy", 23.3, 29.3,21.3, R.string.wi_sunset,3));
        cityList.add(new Cities("London", "UK", 23.3, 29.3,21.3, R.string.wi_snow,3));
        cityList.add(new Cities("New York", "USA", 23.3, 29.3,21.3, R.string.wi_rain,3));
        cityList.add(new Cities("Paris", "France", 23.3, 29.3,21.3, R.string.wi_day_sunny,3));
    }


    // TODO: maybe it should be done in API class

    private Integer findSymbolbyName (String weatherSymbolName) {

        String packageName = getContext().getPackageName();

        Integer identifierSymbol =  getResources().getIdentifier(weatherSymbolName, "string", packageName);
        System.out.println(identifierSymbol);

        return identifierSymbol;
    }
}

