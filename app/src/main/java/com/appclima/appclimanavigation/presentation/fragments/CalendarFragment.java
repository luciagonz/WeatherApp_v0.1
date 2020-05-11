package com.appclima.appclimanavigation.presentation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    CalendarView calendarWidget;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View calendarView = inflater.inflate(R.layout.fragment_calendar, container, false);


        // Find calendar on Calendar Fragment by ID:
        calendarWidget = (CalendarView) calendarView.findViewById(R.id.calendar_view_widget);


        // Create new listener to Calendar to know if the user select different day:
        calendarWidget.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                // Format date used in all project: yyyy/mm/dd
                String selectedDate = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);

                Log.d("Listener Calendar: ", selectedDate); // Debug purpose,


                // TODO: In case user change date, events for this day should be display:
            }
        });

        return calendarView;
    }

}