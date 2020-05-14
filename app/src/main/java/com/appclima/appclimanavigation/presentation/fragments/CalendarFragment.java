package com.appclima.appclimanavigation.presentation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;

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



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View calendarView = inflater.inflate(R.layout.fragment_calendar, container, false);


        // Find calendar on Calendar Fragment by ID:
        calendarWidget = (CalendarView) calendarView.findViewById(R.id.calendar_view_widget);
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