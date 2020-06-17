package com.appclima.appclimanavigation.presentation.cardviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManageCalendar;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.fragments.HomeFragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CalendarEventCard extends RecyclerView.Adapter<CalendarEventCard.CalendarEventCardHolder> {

    Context context;
    CalendarEvents calendarEventsList;
    Activity myActivity;
    Fragment myFragment;
    boolean allDayEvent;
    boolean recurringEvent;
    String recurringRule;



    public CalendarEventCard(Context context, CalendarEvents calendarEventsList, Activity myActivity, Fragment myFragment) {
        this.context = context;
        this.calendarEventsList = calendarEventsList;
        this.myActivity = myActivity;
        this.myFragment = myFragment;

    }


    // Set values to objects in the cardview:

    public class CalendarEventCardHolder extends RecyclerView.ViewHolder {

        TextView startHourEvent;
        TextView endHourEvent;
        TextView titleEvent;
        TextView descriptionEvent;
        CardView cardView;
        ImageView locationIcon;
        ImageView allDayIcon;
        TextView allDayText;



        public CalendarEventCardHolder(@NonNull View itemView) {
            super(itemView);

            startHourEvent = itemView.findViewById(R.id.start_hour_event_cv);
            endHourEvent = itemView.findViewById(R.id.end_hour_event_cv);
            titleEvent =  itemView.findViewById(R.id.title_event_cv);
            descriptionEvent = itemView.findViewById(R.id.description_event_cv);
            cardView = itemView.findViewById(R.id.calendar_event_cv);
            locationIcon = itemView.findViewById(R.id.icon_location_event_cv);
            allDayIcon = itemView.findViewById(R.id.all_day_event_icon);
            allDayText =itemView.findViewById(R.id.all_day_event_text);
        }
    }

    @NonNull

    @Override
    public CalendarEventCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // When a card is created:
        View eventCardView = LayoutInflater.from(context).inflate(R.layout.cardview_calendar_event, parent, false);
        return new CalendarEventCardHolder(eventCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarEventCardHolder holder, final int position) {

        holder.cardView.setCardBackgroundColor((calendarEventsList.getEventColor().get(position)));

        if (calendarEventsList.getAllDayFlagEvent().get(position)) {
            holder.startHourEvent.setVisibility(View.INVISIBLE);
            holder.endHourEvent.setVisibility(View.INVISIBLE);
            holder.allDayText.setVisibility(View.VISIBLE);
            holder.allDayIcon.setVisibility(View.VISIBLE);
        }
        else {
            holder.startHourEvent.setVisibility(View.VISIBLE);
            holder.endHourEvent.setVisibility(View.VISIBLE);
            holder.allDayText.setVisibility(View.INVISIBLE);
            holder.allDayIcon.setVisibility(View.INVISIBLE);
        }


        holder.descriptionEvent.setText(calendarEventsList.getDescriptions().get(position));
        holder.titleEvent.setText(calendarEventsList.getNameOfEvent().get(position));

        // Split dates to adapt to format hh:mm
        holder.startHourEvent.setText(calendarEventsList.getStartDates().get(position).split(" ")[1].split(":")[0] + ":" + calendarEventsList.getStartDates().get(position).split(" ")[1].split(":")[1]);
        holder.endHourEvent.setText(calendarEventsList.getEndDates().get(position).split(" ")[1].split(":")[0] + ":" + calendarEventsList.getEndDates().get(position).split(" ")[1].split(":")[1]);



        if(calendarEventsList.getEventLocation().get(position).isEmpty()) {
            holder.locationIcon.setVisibility(View.INVISIBLE);
        }

        else {
            holder.locationIcon.setVisibility(View.VISIBLE);
        }



        // Listener button OK
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // custom dialog with all text, switch and buttons:
                final Dialog eventDialog = new Dialog(context);
                eventDialog.setContentView(R.layout.dialog_alert_calendar);

                // Show not necessary elements for new event card:
                eventDialog.findViewById(R.id.delete_event_button).setVisibility(View.VISIBLE);
                eventDialog.findViewById(R.id.delete_event_text).setVisibility(View.VISIBLE);
                eventDialog.findViewById(R.id.weather_location_text).setVisibility(View.VISIBLE);

                final TextView eventName = eventDialog.findViewById(R.id.event_name_calendar);
                eventName.setText(calendarEventsList.getNameOfEvent().get(position));

                final TextView eventDescription = eventDialog.findViewById(R.id.event_description_alert);
                eventDescription.setText(calendarEventsList.getDescriptions().get(position));

                final TextView eventStart = eventDialog.findViewById(R.id.event_start_alert);
                final TextView eventEnd = eventDialog.findViewById(R.id.event_end_alert);


                if (calendarEventsList.getAllDayFlagEvent().get(position)) {
                    eventStart.setText(calendarEventsList.getStartDates().get(position).split(" ")[0]);
                    eventEnd.setText(calendarEventsList.getEndDates().get(position).split(" ")[0]);
                }

                else{
                    eventStart.setText(calendarEventsList.getStartDates().get(position));
                    eventEnd.setText(calendarEventsList.getEndDates().get(position));
                }


                final TextView locationEvent = eventDialog.findViewById(R.id.event_location);
                locationEvent.setText(calendarEventsList.getEventLocation().get(position));

                TextView locationEventWeather = eventDialog.findViewById(R.id.event_weather_location);
                locationEventWeather.setText(calendarEventsList.getWeatherDescriptionEvent().get(position));

                TextView eventCalendarAccount = eventDialog.findViewById(R.id.event_calendar_account);
                eventCalendarAccount.setText(String.valueOf(calendarEventsList.getCalendarName().get(position)));
                eventCalendarAccount.setBackgroundColor(calendarEventsList.getEventColor().get(position));

                // Date pickers for start and end date:

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                Date endDate = null;
                Date beginDate = null;
                try {
                    endDate = sdf.parse(calendarEventsList.getEndDates().get(position));
                    beginDate = sdf.parse(calendarEventsList.getStartDates().get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final Calendar beginDateCal = Calendar.getInstance();
                final Calendar endDateCal = Calendar.getInstance();

                beginDateCal.setTime(beginDate);
                endDateCal.setTime(endDate);
                showDateTimePicker(beginDateCal, eventStart);
                showDateTimePicker(endDateCal, eventEnd);



                showDateTimePicker(endDateCal, eventEnd);

                ImageView deleteEvent = eventDialog.findViewById(R.id.delete_event_button);
                deleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialThemeDialog);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you sure you want to delete " + calendarEventsList.getNameOfEvent().get(position) + "?");

                        // In case cancel button is clicked (no changes applied):
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        });

                        // In case OK button is clicked (delete event):
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // Delete event
                                ManageCalendar manageCalendarDelete = new ManageCalendar(context, myActivity);
                                manageCalendarDelete.deleteEvent(calendarEventsList.getEventID().get(position), calendarEventsList.getCalendarID().get(position));
                                // Update fragment to show changes
                                myFragment.getFragmentManager().beginTransaction().detach(myFragment).attach(myFragment).commit();
                                eventDialog.dismiss();

                            }
                        });

                        // Create dialog:
                        AlertDialog confirmationDialog = builder.create();
                        confirmationDialog.show();
                    }
                });


                // Change switch state depending on all_day attribute
                Switch allDaySwitch = eventDialog.findViewById(R.id.all_day_event_switch);
                Switch recurrentEventSwitch = eventDialog.findViewById(R.id.recurring_event_switch);

                if (!(calendarEventsList.getRecurringRule().get(position) == null)) {
                    recurringEvent = true;
                    recurrentEventSwitch.setChecked(true);
                }

                else {
                    recurringEvent = false;
                    recurrentEventSwitch.setChecked(false);
                }


                if (calendarEventsList.getAllDayFlagEvent().get(position)) {
                    allDayEvent = true;
                    allDaySwitch.setChecked(true);
                }

                else {
                    allDayEvent = false;
                    allDaySwitch.setChecked(false);
                }

                final ManageCalendar manageCalendar = new ManageCalendar(context, myActivity);
                changeAllDaySwitch(allDaySwitch, manageCalendar);
                changeRecurringEvent(recurrentEventSwitch);


                // OK button to save calendar changes and close dialog:
                Button positiveButton = eventDialog.findViewById(R.id.ok_button_event_dialog);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                        Calendar beginDateCal = Calendar.getInstance();
                        Calendar endDateCal = Calendar.getInstance();
                        manageCalendar.setCreateEventAllDayChecked(allDayEvent);

                        String stringStart = eventStart.getText().toString();
                        String stringEnd = eventEnd.getText().toString();

                        beginDateCal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(stringStart.split("/")[0]));
                        beginDateCal.set(Calendar.MONTH, Integer.valueOf(stringStart.split("/")[1]) -1);
                        beginDateCal.set(Calendar.YEAR, Integer.valueOf(stringStart.split("/")[2].split(" ")[0]));

                        endDateCal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(stringEnd.split("/")[0]));
                        endDateCal.set(Calendar.MONTH, Integer.valueOf(stringEnd.split("/")[1]) -1);
                        endDateCal.set(Calendar.YEAR, Integer.valueOf(stringEnd.split("/")[2].split(" ")[0]));

                        // To solve all day = true --> all day = false (without enter hours):

                        beginDateCal.set(Calendar.HOUR, 0);
                        beginDateCal.set(Calendar.MINUTE, 0);
                        beginDateCal.set(Calendar.SECOND, 0);
                        endDateCal.set(Calendar.HOUR, 23);
                        endDateCal.set(Calendar.MINUTE, 59);
                        endDateCal.set(Calendar.SECOND, 0);

                        // If has more than 10 characters, user has selected hours:
                        if (!allDayEvent) {
                            if (stringStart.length() > 10) {
                                beginDateCal.set(Calendar.HOUR, Integer.valueOf(stringStart.split(" ")[1].split(":")[0]));
                                beginDateCal.set(Calendar.MINUTE, Integer.valueOf(stringStart.split(" ")[1].split(":")[1]));
                                endDateCal.set(Calendar.HOUR, Integer.valueOf(stringEnd.split(" ")[1].split(":")[0]));
                                endDateCal.set(Calendar.MINUTE, Integer.valueOf(stringEnd.split(" ")[1].split(":")[1]));
                            }
                        }



                        manageCalendar.updateEvent(calendarEventsList.getEventID().get(position), beginDateCal, endDateCal,
                                eventName.getText().toString(), eventDescription.getText().toString(), locationEvent.getText().toString(),
                                calendarEventsList.getCalendarID().get(position), recurringRule);

                        Toast.makeText(context, "Event changed", Toast.LENGTH_SHORT).show();
                        myFragment.getFragmentManager().beginTransaction().detach(myFragment).attach(myFragment).commit();
                        eventDialog.dismiss();


                    }
                });

                // Cancel button to close dialog without changing calendar events:
                Button negativeButton = eventDialog.findViewById(R.id.cancel_button_event_dialog);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventDialog.dismiss();
                    }
                });

                // Show dialog
                eventDialog.show();
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

                    timePicker = new TimePickerDialog(context, R.style.timePicker, timePickerStart, date.get(Calendar.HOUR),
                            date.get(Calendar.MINUTE), false);

                    timePicker.show();
                }
            }
        };

        // If button click, create start date:
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePicker = new DatePickerDialog(context, R.style.datePicker , datePickerStart, date
                        .get(Calendar.YEAR), date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return calendarEventsList.getNameOfEvent().size();
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
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

    private void changeRecurringEvent (Switch recurringEvent) {

        recurringEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // If switch is on, set True tu manageCalendar variable to pass the value to the event:
                if(isChecked) {
                    Log.d("Switch Clicked", "Recurring rule = " + true);
                    recurringRule = "FREQ=DAILY;WKST=SU";
                }
                else {
                    Log.d("Switch Clicked", "Recurring rule = " + false);
                    recurringRule = null;
                }
            }
        });
    }
}

