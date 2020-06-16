package com.appclima.appclimanavigation.presentation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.appclima.appclimanavigation.control.ManageCalendar;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.cardviews.CalendarEventCard;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

    CalendarView calendarWidget;
    TextView titleCalendarEvents;
    MainActivity myActivity;
    CalendarEvents calendarEventsList;
    RecyclerView recyclerEventCards;
    Date selectedDate;
    View calendarView;
    boolean allDayEvent;
    Button addEventButton;




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

        calendarView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Find calendar on Calendar Fragment by ID:
        calendarWidget = (CalendarView) calendarView.findViewById(R.id.calendar_view_widget);
        titleCalendarEvents = (TextView) calendarView.findViewById(R.id.title_calendar_events);
        addEventButton = calendarView.findViewById(R.id.add_event_calendar_button);
        setFirstDay();

        // Get event list first time:
        selectedDate = Calendar.getInstance().getTime();
        getCalendarEvents(selectedDate);
        // Get event list when user changes the day:
        onChangeSelectedDay();
        // Open new dialog if on click
        openNewEventDialogOnClick();





        return calendarView;
    }

    private void onChangeSelectedDay () {
        // Create new listener to Calendar to know if the user select different day:
        calendarWidget.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, month);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = calendarDate.getTime();
                getCalendarEvents(selectedDate);
            }
        });
    }


    private void getCalendarEvents(Date date) {

        ManageCalendar manageCalendar = new ManageCalendar(getContext(), getActivity());
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yy");

        // Print title with date:
        titleCalendarEvents.setText("Your appointments for " + sf.format(date));

        // Get calendar list:
        try {
            calendarEventsList = manageCalendar.getCalendarEvent(sf.format(date), sf.format(date));
            System.out.println(calendarEventsList.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(calendarEventsList);

        // Define linear layout manager:
        LinearLayoutManager layoutManagerEvents= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // find recycler view in calendar fragment by ID
        recyclerEventCards = (RecyclerView) calendarView.findViewById(R.id.recycler_events_RV_calendar);

        // We need to use getContext because we are not in MainActivity but in fragment:
        recyclerEventCards.setLayoutManager(layoutManagerEvents);

        // Adapt information to cardview
        CalendarEventCard calendarEventCard = new CalendarEventCard(getContext(), calendarEventsList, getActivity(), this);

        recyclerEventCards.setAdapter(calendarEventCard);

    }

    private void setFirstDay () {
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

    private void openNewEventDialogOnClick() {
        addEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Button clicked", "New event - Calendar");
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
                        getFragmentManager().beginTransaction().detach(CalendarFragment.this).attach(CalendarFragment.this).commit();

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