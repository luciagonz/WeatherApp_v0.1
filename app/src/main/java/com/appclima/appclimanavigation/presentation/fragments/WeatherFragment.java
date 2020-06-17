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
import com.appclima.appclimanavigation.control.APIWeather;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.model.MyPrefs;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.cardviews.CalendarEventCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherCityCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherScreenCityCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class WeatherFragment extends Fragment {

    RecyclerView recyclerWeatherCard;
    ArrayList<Cities> cityList;
    ArrayList<ForecastCity> cityForecastList;
    MainActivity myActivity;
    MyPrefs myPreferences;


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

    @Override
    public void onResume() {
        super.onResume();
        if (myActivity.isAllowRefresh()) {
            myActivity.setAllowRefresh(false);
            System.out.println("Refresh weather fragment");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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
            WeatherScreenCityCard weatherScreenCityCard = new WeatherScreenCityCard(getContext(), cityList, cityForecastList, myActivity);

            recyclerWeatherCard.setAdapter(weatherScreenCityCard);

            ManagePreferences managePreferences = new ManagePreferences(getContext());
            System.out.println("Initiate weather fragment in " + managePreferences.getManagerLayoutPosition() + " position");

            recyclerWeatherCard.getLayoutManager().smoothScrollToPosition(recyclerWeatherCard, new RecyclerView.State(), managePreferences.getManagerLayoutPosition());

            // Set Indicator to the scroll horizontal view:
            ScrollingPagerIndicator recyclerIndicator = myView.findViewById(R.id.indicator_weather_screen_cities_RV);
            recyclerIndicator.attachToRecyclerView(recyclerWeatherCard);

        }
    }



    private void getCardInformation() {
        ManagePreferences managePreferences = new ManagePreferences(getContext());

        // Access to arrayLists with information about cities (names and type):
        String citiesNames = managePreferences.getPreferences("UserPrefs","citiesNames");
        List<String> cityListNames = Arrays.asList(citiesNames.split(","));
        Log.d("Preferences name cities", cityListNames.toString());

        String citiesTypes = managePreferences.getPreferences("UserPrefs","citiesTypes");
        List<String> cityTypesString = Arrays.asList(citiesTypes.split(","));
        Log.d("Preferences name cities", cityTypesString.toString());

        cityList = new ArrayList<>();
        cityForecastList = new ArrayList<>();

        if (citiesNames.length() > 1) {
            for (int i = 0; i < cityListNames.size(); i++) {

                Log.d("City: ", cityListNames.get(i) + " type " + cityTypesString.get(i));

                APIWeather apiWeather = new APIWeather(cityListNames.get(i), Integer.valueOf(cityTypesString.get(i)), getContext());
                boolean isCityInformationCorrect = apiWeather.manageInformationRequest(true);

                if (isCityInformationCorrect) {
                    cityList.add(apiWeather.getMyCityObject());
                    cityForecastList.add(apiWeather.getMyForecastCity());
                }

            }
        }

        Log.d("Current weather array: ", String.valueOf(cityList.size()));
        Log.d("Forecast weather array:", String.valueOf(cityForecastList.size()));
    }

}