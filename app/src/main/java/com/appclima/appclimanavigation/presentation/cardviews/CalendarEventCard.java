package com.appclima.appclimanavigation.presentation.cardviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

                TextView eventName = eventDialog.findViewById(R.id.event_name_calendar);
                eventName.setText(calendarEventsList.getNameOfEvent().get(position) + " (" + calendarEventsList.getEventID().get(position) + ")");

                TextView eventDescription = eventDialog.findViewById(R.id.event_description_alert);
                eventDescription.setText(calendarEventsList.getDescriptions().get(position));

                TextView eventStart = eventDialog.findViewById(R.id.event_start_alert);
                eventStart.setText(calendarEventsList.getStartDates().get(position));

                TextView eventEnd = eventDialog.findViewById(R.id.event_end_alert);
                eventEnd.setText(calendarEventsList.getEndDates().get(position));

                TextView locationEvent = eventDialog.findViewById(R.id.event_location);
                locationEvent.setText(calendarEventsList.getEventLocation().get(position));

                TextView locationEventWeather = eventDialog.findViewById(R.id.event_weather_location);
                locationEventWeather.setText(calendarEventsList.getWeatherDescriptionEvent().get(position));

                TextView eventCalendarAccount = eventDialog.findViewById(R.id.event_calendar_account);
                eventCalendarAccount.setText(String.valueOf(calendarEventsList.getCalendarName().get(position)));
                eventCalendarAccount.setBackgroundColor(calendarEventsList.getEventColor().get(position));

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
                                ManageCalendar manageCalendar = new ManageCalendar(context, myActivity);
                                manageCalendar.deleteEvent(calendarEventsList.getEventID().get(position));
                                eventDialog.dismiss();

                            }
                        });

                        // Create dialog:
                        AlertDialog confirmationDialog = builder.create();
                        confirmationDialog.show();
                        confirmationDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        confirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    }
                });


                // Change switch state depending on all_day attribute
                Switch allDaySwitch = eventDialog.findViewById(R.id.all_day_event_switch);

                if (calendarEventsList.getAllDayFlagEvent().get(position)) {
                    allDaySwitch.setChecked(true);
                }

                else {
                    allDaySwitch.setChecked(false);
                }

                // OK button to save calendar changes and close dialog:
                Button positiveButton = eventDialog.findViewById(R.id.ok_button_event_dialog);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: Change event

                        Toast.makeText(context, "Event changed", Toast.LENGTH_SHORT).show();

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

    @Override
    public int getItemCount() {

        return calendarEventsList.getNameOfEvent().size();
    }
}

