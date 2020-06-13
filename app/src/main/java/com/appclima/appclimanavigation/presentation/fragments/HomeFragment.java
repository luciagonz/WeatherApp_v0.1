package com.appclima.appclimanavigation.presentation.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.APIWeather;
import com.appclima.appclimanavigation.control.ManageCalendar;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.cardviews.CalendarEventCard;
import com.appclima.appclimanavigation.presentation.cardviews.WeatherCityCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class HomeFragment extends Fragment {

    // https://developer.android.com/guide/components/fragments


    RecyclerView recyclerWeatherCards;
    ArrayList<Cities> cityList;
    ArrayList<ForecastCity> cityForecastList;
    CalendarEvents calendarEventsList;
    RecyclerView recyclerEventCards;
    MainActivity myActivity;
    TextView username_text;
    Button addEventButton;



    // Empty constructor:
    public HomeFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Home Fragment", "onCreate Method");
        // this fragments is part of main activity
        myActivity = (MainActivity) getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (myActivity.isAllowRefresh()) {
            myActivity.setAllowRefresh(false);
            System.out.println("Refresh home fragment");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getCardWeatherInformation();
        getCardCalendarInformation();

        // Set the fragment to the layout:
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        ManagePreferences managePreferences = new ManagePreferences(getContext());
        username_text = homeView.findViewById(R.id.user_name);
        username_text.setText("Hello, " + managePreferences.getUserName() + "!");
        addEventButton = homeView.findViewById(R.id.add_event_calendar_button_home);

        addEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog with all text, switch and buttons:
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_alert_calendar);

                TextView eventName = dialog.findViewById(R.id.event_name_calendar);
                eventName.setText("");

                TextView eventDescription = dialog.findViewById(R.id.event_description_alert);
                eventDescription.setText("");

                TextView eventStart = dialog.findViewById(R.id.event_start_alert);
                eventStart.setText("");

                TextView eventEnd = dialog.findViewById(R.id.event_end_alert);
                eventEnd.setText("");

                TextView locationEvent = dialog.findViewById(R.id.event_location);
                locationEvent.setText("");

                TextView locationEventWeather = dialog.findViewById(R.id.event_weather_location);
                locationEventWeather.setText("");

                TextView eventCalendarAccount = dialog.findViewById(R.id.event_calendar_account);
                eventCalendarAccount.setText(String.valueOf(1));

                // eventCalendarAccount.setBackgroundColor(calendarEventsList.getEventColor().get(position));


                // Change switch state depending on all_day attribute
                Switch allDaySwitch = dialog.findViewById(R.id.all_day_event_switch);
                allDaySwitch.setChecked(false);

                // OK button to save calendar changes and close dialog:
                Button positiveButton = dialog.findViewById(R.id.ok_button_event_dialog);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Change event

                        Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                // Cancel button to close dialog without changing calendar events:
                Button negativeButton = dialog.findViewById(R.id.cancel_button_event_dialog);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Show dialog
                dialog.show();
            }
        });

        initializeRVWeather(homeView);

        // List view
        return homeView;
    }

    private void initializeRVWeather (View myView) {


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

        if(calendarEventsList.getNameOfEvent().size() > 0) {


            // Define linear layout manager:
            LinearLayoutManager layoutManagerEvents= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            // find recycler view in home fragment by ID
            recyclerEventCards = (RecyclerView) myView.findViewById(R.id.recycler_events_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerEventCards.setLayoutManager(layoutManagerEvents);

            // Adapt information to cardview
            CalendarEventCard calendarEventCard = new CalendarEventCard(getContext(), calendarEventsList, getActivity(), this);

            recyclerEventCards.setAdapter(calendarEventCard);

            // Set Indicator to the scroll horizontal view:
            // ScrollingPagerIndicator recyclerIndicator = myView.findViewById(R.id.indicator_weather_cities_RV);
            // recyclerIndicator.attachToRecyclerView(recyclerWeatherCards);

        }



    }


    private void getCardCalendarInformation () {

        // In home screen we will always show today's events:
        SimpleDateFormat today = new SimpleDateFormat("MM/dd/yy");

        ManageCalendar manageCalendar = new ManageCalendar(getContext(), getActivity());


        try {
            calendarEventsList = manageCalendar.getCalendarEvent(today.format(new Date()), today.format(new Date()));
            System.out.println(calendarEventsList.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    private void getCardWeatherInformation() {
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

        for (int i = 0; i < cityListNames.size(); i++) {

            Log.d("City: ", cityListNames.get(i) +  " type " + Integer.valueOf(cityTypesString.get(i)));

            APIWeather apiWeather = new APIWeather(cityListNames.get(i), Integer.valueOf(cityTypesString.get(i)), getContext());
            boolean isCityInformationCorrect = apiWeather.manageInformationRequest(true);

            if (isCityInformationCorrect) {
                cityList.add(apiWeather.getMyCityObject());
                cityForecastList.add(apiWeather.getMyForecastCity());
            }

        }

        Log.d("Current weather array: ", String.valueOf(cityList.size()));
        Log.d("Forecast weather array:", String.valueOf(cityForecastList.size()));
    }

}


