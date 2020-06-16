package com.appclima.appclimanavigation.presentation.cardviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.utilities.Font_icons;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherCityCard extends RecyclerView.Adapter<WeatherCityCard.WeatherCityCardHolder> {

    Context myContext;
    List<Cities> myCityList;
    List<ForecastCity> myCityForecastList;
    Activity myActivity;


    public WeatherCityCard(Context context, List<Cities> city, List<ForecastCity> cityForecast, Activity myActivity) {
        this.myContext = context;
        this.myCityList = city;
        this.myCityForecastList = cityForecast;
        this.myActivity = myActivity;

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
    public void onBindViewHolder(@NonNull WeatherCityCardHolder holder, final int position) {
        // CURRENT WEATHER INFORMATION:
        // Update information from City model:
        Cities myCity = myCityList.get(position);
        ForecastCity myCityForecast = myCityForecastList.get(position);
        System.out.println(position);

        List<String> time_text = new ArrayList<>();
        List<Double> range_temp = new ArrayList<>();
        List<Integer> icons_ID = new ArrayList<>();



        for (int j = 0; j < myCityForecast.getTime_text().size(); j++){
            if(myCityForecast.getTime_text().get(j).contains("15:00:00")){
                // Get data without time:
                String data_text = myCityForecast.getTime_text().get(j).substring(0, myCityForecast.getTime_text().get(j).indexOf(" "));
                // Get data without year (dd-mm)
                time_text.add((data_text.split("-")[2]) + "-" + (data_text.split("-")[1]));
                icons_ID.add(myCityForecast.getWeatherIconID_forecast().get(j));
                range_temp.add((myCityForecast.getTemp_max_forecast().get(j)));
            }
        }



        System.out.println("DATA RECOVER IN FRAGMENT:");
        System.out.println(time_text);
        System.out.println(icons_ID);
        System.out.println(range_temp);

        holder.name.setText(myCity.getName());
        holder.weatherDescription.setText(myCity.getWeatherDescription());
        Integer symbolDirectory = myCity.getSymbolWeatherID();
        // Set icon from id:
        holder.currentsymbolWeather.setText(symbolDirectory);



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
        holder.symbolWeather_1day.setText(icons_ID.get(0));
        holder.symbolWeather_2day.setText(icons_ID.get(1));
        holder.symbolWeather_3day.setText(icons_ID.get(2));
        holder.symbolWeather_4day.setText(icons_ID.get(3));
        holder.symbolWeather_5day.setText(icons_ID.get(4));

        // Temperature depending on units:
        ManagePreferences myPrefs = new ManagePreferences(myContext);

        String unitTempPrefs = myPrefs.getDefaultUnitTemperature();


        if(unitTempPrefs.contains("C")) {

            holder.rangeTemperatures_1day.setText((String.format("%.1f", range_temp.get(0) - 273.15)) + "ºC");
            holder.rangeTemperatures_2day.setText((String.format("%.1f", range_temp.get(1) - 273.15)) + "ºC");
            holder.rangeTemperatures_3day.setText((String.format("%.1f", range_temp.get(2) - 273.15))+ "ºC");
            holder.rangeTemperatures_4day.setText((String.format("%.1f", range_temp.get(3) - 273.15))+ "ºC");
            holder.rangeTemperatures_5day.setText((String.format("%.1f", range_temp.get(4) - 273.15))+ "ºC");

            holder.currentDegrees.setText((String.format("%.1f", myCity.getCurrentTemperature()-273.15)) + "ºC");
            holder.currentmaxDegrees.setText((String.format("%.1f", myCity.getMaxTemperature()-273.15)) + "ºC");
            holder.currentminDegrees.setText((String.format("%.1f", myCity.getMinTemperature()-273.15)) + "ºC");
        }

        else if (unitTempPrefs.contains("F")) {

            holder.rangeTemperatures_1day.setText((String.format("%.1f",range_temp.get(0)*9/5 - 459.67)) + "ºF");
            holder.rangeTemperatures_2day.setText((String.format("%.1f",range_temp.get(1)*9/5 - 459.67)) + "ºF");
            holder.rangeTemperatures_3day.setText((String.format("%.1f",range_temp.get(2)*9/5 - 459.67))+ "ºF");
            holder.rangeTemperatures_4day.setText((String.format("%.1f",range_temp.get(3)*9/5 - 459.67))+ "ºF");
            holder.rangeTemperatures_5day.setText((String.format("%.1f", range_temp.get(4)*9/5 - 459.67))+ "ºF");

            // Convert double to string before set the value:
            holder.currentDegrees.setText((String.format("%.1f", myCity.getCurrentTemperature()*9/5 - 459.67)) + "ºF");
            holder.currentmaxDegrees.setText((String.format("%.1f", myCity.getMaxTemperature()*9/5 - 459.67)) + "ºF");
            holder.currentminDegrees.setText((String.format("%.1f", myCity.getMinTemperature()*9/5 - 459.67)) + "ºF");
        }

        else { // Kelvin (default in API)
            holder.rangeTemperatures_1day.setText(String.format("%.1f", range_temp.get(0)) + "ºK");
            holder.rangeTemperatures_2day.setText(String.format("%.1f", range_temp.get(1)) + "ºK");
            holder.rangeTemperatures_3day.setText(String.format("%.1f", range_temp.get(2)) + "ºK");
            holder.rangeTemperatures_4day.setText(String.format("%.1f", range_temp.get(3)) + "ºK");
            holder.rangeTemperatures_5day.setText(String.format("%.1f", range_temp.get(4)) + "ºK");

            // Convert double to string before set the value:
            holder.currentDegrees.setText((String.format("%.0f", myCity.getCurrentTemperature())) + "ºK");
            holder.currentmaxDegrees.setText((String.format("%.0f", myCity.getMaxTemperature())) + "ºK");
            holder.currentminDegrees.setText((String.format("%.0f", myCity.getMinTemperature())) + "ºK");
        }

        holder.date_1day.setText(time_text.get(0));
        holder.date_2day.setText(time_text.get(1));
        holder.date_3day.setText(time_text.get(2));
        holder.date_4day.setText(time_text.get(3));
        holder.date_5day.setText(time_text.get(4));

        holder.currentsymbolWeather.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println(position);
                ManagePreferences managePreferences = new ManagePreferences(myContext);
                managePreferences.setManagerLayoutPosition(position);
                BottomNavigationView myBottomNavigationView = myActivity.findViewById(R.id.nav_view);
                System.out.println(myBottomNavigationView);
                myBottomNavigationView.setSelectedItemId(R.id.navigation_weather);
            }
        });

        holder.removeCityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Remove city: " + position);
                AlertDialog dialog = new AlertDialog.Builder(myContext, R.style.MaterialThemeDialog).create();
                dialog.setTitle("Remove City");
                dialog.setMessage("Are you sure?");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                ManagePreferences managePreferences = new ManagePreferences(myContext);
                                managePreferences.removeLocation(position+1);
                                BottomNavigationView myBottomNavigationView = myActivity.findViewById(R.id.nav_view);
                                System.out.println(myBottomNavigationView);
                                myBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                            }
                        });

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                            }
                        });
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(myContext, R.color.colorPrimaryDark));
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(myContext, R.color.colorPrimaryDark));
            }
        });





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
        public TextView weatherDescription;

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
        public ImageView removeCityIcon;


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
            weatherDescription = (TextView) itemView.findViewById(R.id.weather_description);

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

            removeCityIcon = (ImageView) itemView.findViewById(R.id.city_remove_list);



        }
    }



}

