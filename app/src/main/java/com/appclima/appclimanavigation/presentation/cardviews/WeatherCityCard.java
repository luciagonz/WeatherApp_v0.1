package com.appclima.appclimanavigation.presentation.cardviews;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;

import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherCityCard extends RecyclerView.Adapter<WeatherCityCard.WeatherCityCardHolder> {

    Context context;
    List<Cities> myCityList;

    public WeatherCityCard(Context context, List<Cities> city) {
        this.context = context;
        this.myCityList = city;
    }

    public WeatherCityCard(List<Cities> myCityList) {
        this.myCityList = myCityList;
    }

    @NonNull

    @Override
    public WeatherCityCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // When a card is created:
        View eventCardView = LayoutInflater.from(context).inflate(R.layout.cardview_weather_city, parent, false);
        return new WeatherCityCardHolder(eventCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherCityCardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return myCityList.size();
    }


// Set values to objects in the cardview:

    public class WeatherCityCardHolder extends RecyclerView.ViewHolder {

        // Define font used for icons:
        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.iconFontPath));
        TextView name;
        TextView currentDegrees;
        TextView maxDegrees;
        TextView minDegrees;
        TextView symbolWeather;


        public WeatherCityCardHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.city_text_home);
            currentDegrees = (TextView) itemView.findViewById(R.id.current_degrees_text_home);
            maxDegrees = (TextView) itemView.findViewById(R.id.current_max_temp_text_home);
            minDegrees = (TextView) itemView.findViewById(R.id.current_min_temp_text_home);
            symbolWeather = (TextView) itemView.findViewById(R.id.current_weather_icon_home);
            symbolWeather.setTypeface(font);

        }
    }
}

