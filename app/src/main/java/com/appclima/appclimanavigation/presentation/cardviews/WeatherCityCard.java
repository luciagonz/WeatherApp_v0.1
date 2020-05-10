package com.appclima.appclimanavigation.presentation.cardviews;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.utilities.Font_icons;

import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherCityCard extends RecyclerView.Adapter<WeatherCityCard.WeatherCityCardHolder> {

    Context myContext;
    List<Cities> myCityList;


    public WeatherCityCard(Context context, List<Cities> city) {
        this.myContext = context;
        this.myCityList = city;

    }

    public WeatherCityCard(List<Cities> myCityList) {
        this.myCityList = myCityList;
    }


    @NonNull

    @Override
    public WeatherCityCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // When a card is created:
        System.out.println(myCityList);

        WeatherCityCardHolder eventCardView = new WeatherCityCardHolder(LayoutInflater.from(myContext).inflate(R.layout.cardview_weather_city, parent, false));
        return eventCardView;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherCityCardHolder holder, int position) {
        // CURRENT WEATHER INFORMATION:
        // Update information from City model:
        Cities myCity = myCityList.get(position);
        System.out.println(position);


        holder.name.setText(myCity.getName());
        Integer symbolDirectory = myCity.getSymbolWeatherID();
        // Set icon from id:
        holder.currentsymbolWeather.setText(symbolDirectory);

        // Convert double to string before set the value:
        holder.currentDegrees.setText(String.valueOf(myCity.getCurrentDegrees()));
        holder.currentmaxDegrees.setText(String.valueOf(myCity.getMaxDegrees()));
        holder.currentminDegrees.setText(String.valueOf(myCity.getMinDegrees()));

        // Set drawable location according to its locationType:

        if(myCity.getLocationType() == 1) {
            holder.locationTypeDrawable.setImageResource(R.drawable.location_type_1_gps);
        }
        else if (myCity.getLocationType() == 2) {
            holder.locationTypeDrawable.setImageResource(R.drawable.location_type_2_default);
        }
        else {
            holder.locationTypeDrawable.setImageResource(R.drawable.location_type_3_fav);
        }



        // TODO: Place getters from City forecast information (depending on API responses, define data architecture):

        // FORECAST WEATHER INFORMATION:
        holder.symbolWeather_1day.setText(symbolDirectory);
        holder.symbolWeather_2day.setText(symbolDirectory);
        holder.symbolWeather_3day.setText(symbolDirectory);
        holder.symbolWeather_4day.setText(symbolDirectory);
        holder.symbolWeather_5day.setText(symbolDirectory);


    }

    @Override
    public int getItemCount() {

        return myCityList.size();
    }


// Set values to objects in the cardview:

    public class WeatherCityCardHolder extends RecyclerView.ViewHolder {

        // Current information:
        public TextView name;
        public TextView currentDegrees;
        public TextView currentmaxDegrees;
        public TextView currentminDegrees;
        public TextView currentsymbolWeather;

        // Forecast information:

        // Symbols weather
        public TextView symbolWeather_1day;
        public TextView symbolWeather_2day;
        public TextView symbolWeather_3day;
        public TextView symbolWeather_4day;
        public TextView symbolWeather_5day;

        // Range temperatures
        public TextView rangeTemperatures_1day;
        public TextView rangeTemperatures_2day;
        public TextView rangeTemperatures_3day;
        public TextView rangeTemperatures_4day;
        public TextView rangeTemperatures_5day;

        // Dates
        public TextView date_1day;
        public TextView date_2day;
        public TextView date_3day;
        public TextView date_4day;
        public TextView date_5day;

        // Is this city the default location, the GPS location or has been asked for:
        // Default location: locationType = 1
        // GPS location: locationType = 2;
        // Asked location: locationType = 3;
        public ImageView locationTypeDrawable;




        // Link every textView label with each atribute:
        public WeatherCityCardHolder(@NonNull View itemView) {
            super(itemView);

            System.out.println("entro aqui");

            // Current information:
            name = (TextView) itemView.findViewById(R.id.city_text_home);
            currentDegrees = (TextView) itemView.findViewById(R.id.current_degrees_text_home);
            currentmaxDegrees = (TextView) itemView.findViewById(R.id.current_max_temp_text_home);
            currentminDegrees = (TextView) itemView.findViewById(R.id.current_min_temp_text_home);
            currentsymbolWeather = (TextView) itemView.findViewById(R.id.current_weather_icon_home);

            // Forecast information
            rangeTemperatures_1day = (TextView) itemView.findViewById(R.id.day1_weather_degrees_range);
            rangeTemperatures_2day = (TextView) itemView.findViewById(R.id.day2_weather_degrees_range);
            rangeTemperatures_3day = (TextView) itemView.findViewById(R.id.day3_weather_degrees_range);
            rangeTemperatures_4day = (TextView) itemView.findViewById(R.id.day4_weather_degrees_range);
            rangeTemperatures_5day = (TextView) itemView.findViewById(R.id.day5_weather_degrees_range);

            // Dates
            date_1day = (TextView) itemView.findViewById(R.id.forecast_day1);
            date_2day = (TextView) itemView.findViewById(R.id.forecast_day2);
            date_3day = (TextView) itemView.findViewById(R.id.forecast_day3);
            date_4day = (TextView) itemView.findViewById(R.id.forecast_day4);
            date_5day = (TextView) itemView.findViewById(R.id.forecast_day5);

            // Symbols:
            symbolWeather_1day = (TextView) itemView.findViewById(R.id.day1_weather_icon_home);
            symbolWeather_2day = (TextView) itemView.findViewById(R.id.day2_weather_icon_home);
            symbolWeather_3day = (TextView) itemView.findViewById(R.id.day3_weather_icon_home);
            symbolWeather_4day = (TextView) itemView.findViewById(R.id.day4_weather_icon_home);
            symbolWeather_5day = (TextView) itemView.findViewById(R.id.day5_weather_icon_home);

            // Typeface symbols to show correct value depending on its name (linked in string.xml):
            Typeface font = Typeface.createFromAsset(myContext.getAssets(), myContext.getString(R.string.iconFontPath));
            currentsymbolWeather.setTypeface(font);
            symbolWeather_1day.setTypeface(font);
            symbolWeather_2day.setTypeface(font);
            symbolWeather_3day.setTypeface(font);
            symbolWeather_4day.setTypeface(font);
            symbolWeather_5day.setTypeface(font);

            // Drawable location type:
            locationTypeDrawable = (ImageView) itemView.findViewById(R.id.city_location_type);

        }
    }



}

