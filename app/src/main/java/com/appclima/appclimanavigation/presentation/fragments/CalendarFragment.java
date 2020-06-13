package com.appclima.appclimanavigation.presentation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    CalendarView calendarWidget;
    TextView titleCalendarEvents;
    MainActivity myActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Calendar Fragment", "onCreate Method");
        // this fragments is part of main activity
        myActivity = (MainActivity) getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (myActivity.isAllowRefresh()) {
            myActivity.setAllowRefresh(false);
            System.out.println("Refresh calendar fragment");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View calendarView = inflater.inflate(R.layout.fragment_calendar, container, false);


        // Find calendar on Calendar Fragment by ID:
        calendarWidget = (CalendarView) calendarView.findViewById(R.id.calendar_view_widget);
        ManagePreferences managePreferences = new ManagePreferences(getContext());
        String startOfWeek = managePreferences.getDayOfWeek();

        if (startOfWeek.contains("Monday")) {
            calendarWidget.setFirstDayOfWeek(2);
        }

        if (startOfWeek.contains("Tuesday")) {
            calendarWidget.setFirstDayOfWeek(3);
        }

        if (startOfWeek.contains("Wednesday")) {
            calendarWidget.setFirstDayOfWeek(4);
        }

        if (startOfWeek.contains("Thursday")) {
            calendarWidget.setFirstDayOfWeek(5);
        }

        if (startOfWeek.contains("Friday")) {
            calendarWidget.setFirstDayOfWeek(6);
        }

        if (startOfWeek.contains("Saturday")) {
            calendarWidget.setFirstDayOfWeek(7);
        }

        if (startOfWeek.contains("Sunday")) {
            calendarWidget.setFirstDayOfWeek(1);
        }


        titleCalendarEvents = (TextView) calendarView.findViewById(R.id.title_calendar_events);
        // Get current time and print it to the title:
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        titleCalendarEvents.setText("Your appointments for " + format.format(currentTime));



        // Create new listener to Calendar to know if the user select different day:
        calendarWidget.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                // If the user selects different date, then print the correct title according to the day selected:
                String selectedDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year);
                Log.d("Listener Calendar: ", selectedDate); // Debug purpose
                titleCalendarEvents.setText("Your appointments for " + selectedDate);




                // TODO: In case user change date, events for this day should be display:
            }
        });

        return calendarView;
    }



}