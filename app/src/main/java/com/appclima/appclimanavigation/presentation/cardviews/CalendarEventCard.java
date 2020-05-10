package com.appclima.appclimanavigation.presentation.cardviews;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.CalendarEvents;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CalendarEventCard extends RecyclerView.Adapter<CalendarEventCard.CalendarEventCardHolder> {

    Context context;
    List<CalendarEvents> calendarEventsList;

    public CalendarEventCard(Context context, List<CalendarEvents> calendarEventsList) {
        this.context = context;
        this.calendarEventsList = calendarEventsList;
    }


    // Set values to objects in the cardview:

    public class CalendarEventCardHolder extends RecyclerView.ViewHolder {

        TextView startHourEvent;
        TextView endHourEvent;
        TextView titleEvent;
        TextView locationEvent;


        public CalendarEventCardHolder(@NonNull View itemView) {
            super(itemView);

            startHourEvent = (TextView) itemView.findViewById(R.id.start_hour_event_cv);
            endHourEvent = (TextView) itemView.findViewById(R.id.end_hour_event_cv);
            titleEvent = (TextView) itemView.findViewById(R.id.title_event_cv);
            locationEvent = (TextView) itemView.findViewById(R.id.place_event_cv);
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
    public void onBindViewHolder(@NonNull CalendarEventCardHolder holder, int position) {
        CalendarEvents myEvent = calendarEventsList.get(position);
        holder.titleEvent.setText(myEvent.getTitleEvent());
        holder.locationEvent.setText(myEvent.getLocationEvent());
        holder.startHourEvent.setText(myEvent.getStartHourEvent());
        holder.endHourEvent.setText(myEvent.getEndHourEvent());

    }

    @Override
    public int getItemCount() {

        return calendarEventsList.size();
    }



}

