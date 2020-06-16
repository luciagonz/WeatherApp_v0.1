package com.appclima.appclimanavigation.presentation.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    boolean allDayEvent = false;



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

        openNewEventDialogOnClick();
        initializeRVWeather(homeView);

        // List view
        return homeView;
    }

    private void initializeRVWeather (final View myView) {
        if(cityList !=null) {

            // RECYCLERVIEW CITIES:
            Log.d("Home Fragment View", "initializing view elements...");

            // Define linear layout manager:
            final LinearLayoutManager layoutManagerWeather = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            // find recycler view in home fragment by ID
            recyclerWeatherCards = (RecyclerView) myView.findViewById(R.id.weather_cities_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerWeatherCards.setLayoutManager(layoutManagerWeather);

            ManagePreferences managePreferences = new ManagePreferences(getContext());
            layoutManagerWeather.scrollToPosition(managePreferences.getManagerLayoutPosition());
            managePreferences.setManagerLayoutPosition(0);


            // Adapt information to cardview
            WeatherCityCard weatherCityCard = new WeatherCityCard(getContext(), cityList, cityForecastList, getActivity());

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

    private void openNewEventDialogOnClick() {
        addEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Button clicked", "New event - Home");
                // custom dialog with all text, switch and buttons:
                final Dialog newEventDialog = new Dialog(getContext());
                newEventDialog.setContentView(R.layout.dialog_alert_calendar);

                // Hide not necessary elements for new event card:
                newEventDialog.findViewById(R.id.delete_event_button).setVisibility(View.INVISIBLE);
                newEventDialog.findViewById(R.id.delete_event_text).setVisibility(View.INVISIBLE);
                newEventDialog.findViewById(R.id.weather_location_text).setVisibility(View.INVISIBLE);

                final TextView eventName = newEventDialog.findViewById(R.id.event_name_calendar);
                eventName.setText("");

                final TextView eventDescription = newEventDialog.findViewById(R.id.event_description_alert);
                eventDescription.setText("");

                final TextView eventStart = newEventDialog.findViewById(R.id.event_start_alert);
                eventStart.setText("Select date");
                eventStart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                eventStart.setTextColor(getResources().getColor(R.color.colorPrimaryText));

                final TextView eventEnd = newEventDialog.findViewById(R.id.event_end_alert);
                eventEnd.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                eventEnd.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                eventEnd.setText("Select date");

                final TextView locationEvent = newEventDialog.findViewById(R.id.event_location);
                locationEvent.setText("");

                final TextView locationEventWeather = newEventDialog.findViewById(R.id.event_weather_location);
                locationEventWeather.setText("");

                final TextView eventCalendarAccount = newEventDialog.findViewById(R.id.event_calendar_account);


                final ManageCalendar manageCalendar = new ManageCalendar(getContext(), getActivity());

                // Get default calendar (account, name and color)
                manageCalendar.calendarInformation();
                eventCalendarAccount.setText(String.valueOf(manageCalendar.getCalendarListName().get(0)));
                eventCalendarAccount.setBackgroundColor(manageCalendar.getCalendarListColor().get(0));

                // Restart all day and default account selected
                manageCalendar.setCreateEventAllDayChecked(false);
                manageCalendar.setCreateEventCalendarAccount(manageCalendar.getCalendarListID().get(0));
                displaySelectedCalendarDialog(eventCalendarAccount, manageCalendar);


                // Change switch state depending on all_day attribute
                final Switch allDaySwitch = newEventDialog.findViewById(R.id.all_day_event_switch);
                changeAllDaySwitch(allDaySwitch, manageCalendar);

                // Date pickers for start and end date:
                final Calendar startDate = Calendar.getInstance();
                showDateTimePicker(startDate, eventStart);

                final Calendar endDate = Calendar.getInstance();
                showDateTimePicker(endDate, eventEnd);


                // OK button to save calendar changes and close dialog:
                Button positiveButton = newEventDialog.findViewById(R.id.ok_button_event_dialog);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manageCalendar.createEvent(startDate, endDate,
                                eventName.getText().toString(), eventDescription.getText().toString(),
                                locationEvent.getText().toString());

                        // Update fragment to show changes
                        getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();

                        // Show toast to notify to the user:
                        Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT).show();

                        newEventDialog.dismiss();
                    }
                });

                // Cancel button to close dialog without changing calendar events:
                Button negativeButton = newEventDialog.findViewById(R.id.cancel_button_event_dialog);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newEventDialog.dismiss();
                    }
                });

                // Show dialog
                newEventDialog.show();
            }
        });
    }

    private void displaySelectedCalendarDialog(final TextView eventCalendarAccount, final ManageCalendar manageCalendar) {
        eventCalendarAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
                builder.setTitle("Select the calendar");
                builder.setIcon(R.drawable.ic_calendar_day_pref_menu);
                Integer checkedItem = 0;
                builder.setSingleChoiceItems(manageCalendar.getCalendarListName().toArray(new String[0]), checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manageCalendar.setCreateEventCalendarAccount(manageCalendar.getCalendarListID().get(which));
                        eventCalendarAccount.setText(manageCalendar.getCalendarListAccount().get(which).split("@")[0]);
                        eventCalendarAccount.setBackgroundColor(manageCalendar.getCalendarListColor().get(which));
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void showDateTimePicker(final Calendar date, final TextView dateText) {
        final TimePickerDialog.OnTimeSetListener timePickerStart = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                dateText.setText(dateText.getText() + " " + hourOfDay + ":" + minute);
            }
        };

        final DatePickerDialog.OnDateSetListener datePickerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, final int month, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                // Create timepicker only if is not an all day event:
                if (!allDayEvent){

                    TimePickerDialog timePicker;

                    timePicker = new TimePickerDialog(getContext(), R.style.timePicker, timePickerStart, date.get(Calendar.HOUR),
                            date.get(Calendar.MINUTE), false);

                    timePicker.show();
                }
            }
        };

        // If button click, create start date:
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.datePicker , datePickerStart, date
                        .get(Calendar.YEAR), date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
                datePicker.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                datePicker.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }


    private void changeAllDaySwitch (Switch allDaySwitch, final ManageCalendar manageCalendar) {

        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // If switch is on, set True tu manageCalendar variable to pass the value to the event:
                if(isChecked) {
                    Log.d("Switch Clicked", "All day = " + true);
                    manageCalendar.setCreateEventAllDayChecked(true);
                    allDayEvent = true;
                }
                else {
                    Log.d("Switch Clicked", "All day = " + false);
                    manageCalendar.setCreateEventAllDayChecked(false);
                    allDayEvent = false;

                }
            }
        });
    }
}
