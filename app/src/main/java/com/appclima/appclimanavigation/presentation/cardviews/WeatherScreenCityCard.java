package com.appclima.appclimanavigation.presentation.cardviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.utilities.TemperatureMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class WeatherScreenCityCard extends RecyclerView.Adapter<WeatherScreenCityCard.WeatherScreenCityCardHolder> {
    Context myContext;
    List<Cities> myCityList;
    List<ForecastCity> myCityForecastList;
    double currentTemp;
    double maxTemp;
    double minTemp;
    double feelsTemp;
    double windSpeed;
    MainActivity myActivity;
    int myCityPosition;



    public WeatherScreenCityCard(Context context, List<Cities> city, List<ForecastCity> cityForecast, MainActivity activity) {
        Log.d("WeatherScreenCityCard", " passing values");
        this.myContext = context;
        this.myCityList = city;
        this.myCityForecastList = cityForecast;
        this.myActivity = activity;

    }

    @NonNull
    @Override
    public WeatherScreenCityCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // When a card is created:
        Log.d("WeatherScreenCityCard", "Inflating views...");
        WeatherScreenCityCardHolder weatherCardView = new WeatherScreenCityCardHolder(LayoutInflater.from(myContext).inflate(R.layout.cardview_weather_screen, parent, false));
        return weatherCardView;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherScreenCityCardHolder holder, int position) {

        Cities myCity = myCityList.get(position);
        ForecastCity myForecastCity = myCityForecastList.get(position);

        holder.cityName.setText(myCity.getName());
        // TODO: No las estoy extrayendo de la API, cambiarlo todo y hacerlo:
        holder.coordinatesCity.setText("Coords:" );

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


        // Temperature depending on units:
        ManagePreferences myPrefs = new ManagePreferences(myContext);

        String unitTempPrefs = myPrefs.getDefaultUnitTemperature();

        if(unitTempPrefs.contains("C")) {
            currentTemp =  myCity.getCurrentTemperature() - 273.15;
            maxTemp = myCity.getMaxTemperature() - 273.15;
            minTemp = myCity.getMinTemperature() - 273.15;
            feelsTemp = myCity.getFeelsTemperature() - 273.15;
        }

        else if (unitTempPrefs.contains("F")) {
            currentTemp =  (myCity.getCurrentTemperature()*9/5) - 459.67;
            maxTemp = (myCity.getMaxTemperature()*9/5) - 459.67;
            minTemp = (myCity.getMinTemperature()*9/5) - 459.67;
            feelsTemp = (myCity.getFeelsTemperature()*9/5) - 459.67;
        }

        else { // Kelvin (default in API)
            currentTemp =  myCity.getCurrentTemperature();
            maxTemp = myCity.getMaxTemperature();
            minTemp = myCity.getMinTemperature();
            feelsTemp = myCity.getFeelsTemperature();
        }


        holder.currentDegrees.setText("Current: " + String.format("%.2f", currentTemp) + " º" + unitTempPrefs);
        holder.currentMaxDegrees.setText("Max: " + String.format("%.2f", maxTemp) + " º" + unitTempPrefs);
        holder.currentMinDegrees.setText("Min: " + String.format("%.2f", minTemp) + " º" + unitTempPrefs);
        holder.currentFeelDegrees.setText("Feels: " + String.format("%.2f", feelsTemp) + " º" + unitTempPrefs);


        // Sunrise and sunset time:
        String hour_sunrise = unixToDate(String.valueOf(myCity.getSunrise()));
        String hour_sunset = unixToDate(String.valueOf(myCity.getSunset()));

        holder.sunrise.setText("Sunrise: " + hour_sunrise + " h");
        holder.sunset.setText("Sunset: " + hour_sunset + " h");

        // Wind speed and degrees:

        String windUnitPrefs = myPrefs.getDefaultUnitWind();

        if (windUnitPrefs.contains("km/h")) {

            windSpeed = myCity.getWindSpeed() * 3.6;

        }

        else {

            windSpeed = myCity.getWindSpeed();

        }

        holder.windSpeed.setText("Wind speed: " +  String.format("%.2f", windSpeed) + " " + windUnitPrefs);
        holder.windDeg.setText("Wind deg: " + myCity.getWindDegrees() + " º");



        // Weather description and symbols involved:
        holder.weatherDescription.setText(myCity.getWeatherDescription());
        holder.currentSymbolWeather.setText(myCity.getSymbolWeatherID());
        holder.thermometerWeather.setText(R.string.wi_thermometer);

        // Coordinates:
        holder.coordinatesCity.setText("Lat: " + myCity.getLatitude() + " Long: " + myCity.getLongitude());


        holder.clouds.setText("Clouds: " + myCity.getClouds());
        holder.visibility.setText("Visibility: " + myCity.getVisibility()/1000 + " km");
        holder.pressure.setText("Pressure: " + myCity.getPressure() + " hPa");
        holder.humidity.setText("Humidity: " + myCity.getHumidity() + " %");

        myCityPosition = position;

        holder.removeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Remove city: " + myCityPosition);
                AlertDialog dialog = new AlertDialog.Builder(myContext, R.style.MaterialThemeDialog).create();
                dialog.setTitle("Remove City");
                dialog.setMessage("Are you sure?");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                ManagePreferences managePreferences = new ManagePreferences(myContext);
                                managePreferences.removeLocation(myCityPosition);
                                BottomNavigationView myBottomNavigationView = myActivity.findViewById(R.id.nav_view);
                                System.out.println(myBottomNavigationView);
                                myBottomNavigationView.setSelectedItemId(R.id.navigation_weather);
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



        renderData(myForecastCity, holder.forecastChart_max, myForecastCity.getTemp_forecast(), true);

        ArrayList<Double> humidityDouble = new ArrayList<>();
        for (int i=0; i<myForecastCity.getHumidity_forecast().size(); ++i) {
            humidityDouble.add((double) myForecastCity.getHumidity_forecast().get(i));
        }

        System.out.println(humidityDouble);
        renderData(myForecastCity, holder.forecastChart_min, humidityDouble, false);


    }

    public void renderData(ForecastCity myForecastCity, LineChart forecastChart, List<Double> data, boolean isTemp) {

        ArrayList<Entry> entries = new ArrayList<>();
        ManagePreferences myPrefs = new ManagePreferences(myContext);

        String unitTempPrefs = myPrefs.getDefaultUnitTemperature();
        Float ymax = 0f;
        Float ymin = 0f;
        Float yaverage = 0f;

        for (int i = 0; i < data.size(); i++) {
            if(isTemp) {
                if(unitTempPrefs.contains("C")) {
                    entries.add(new Entry(i+1, (float) (data.get(i).floatValue()-273.15)));
                    ymax = (float) (myForecastCity.getMax(data)-273.15);
                    ymin = (float) (myForecastCity.getMin(data) -273.15);
                    yaverage = (float) (myForecastCity.getAverage(data)-273.15);

                }

                else if (unitTempPrefs.contains("F")) {
                    entries.add(new Entry(i+1, (float) ((data.get(i).floatValue()*9/5) - 459.67)));
                    ymax = (float) ((myForecastCity.getMax(data)*9/5) - 459.67);
                    ymin = (float) ((myForecastCity.getMin(data)*9/5) - 459.67);
                    yaverage = (float) ((myForecastCity.getAverage(data)*9/5) - 459.67);
                }

                else { // Kelvin (default in API)
                    entries.add(new Entry(i+1, (data.get(i).floatValue())));
                    ymax = (float) myForecastCity.getMax(data);
                    ymin = (float) myForecastCity.getMin(data);
                    yaverage = (float) myForecastCity.getAverage(data);

                }
            }

            else { // humidity
                entries.add(new Entry(i+1, (data.get(i).floatValue())));
                ymax = (float) myForecastCity.getMax(data);
                ymin = (float) myForecastCity.getMin(data);
                yaverage = (float) myForecastCity.getAverage(data);

            }
        }

        System.out.println("Min " + ymin + " max " + ymax + " average " + yaverage);


        System.out.println(entries.toString());



        forecastChart.getLegend().setEnabled(false);
        forecastChart.getDescription().setEnabled(false);

        XAxis xAxis = forecastChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setEnabled(false);
        xAxis.setAxisMaximum(myForecastCity.getTime_text().size());
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLimitLinesBehindData(true);

        LimitLine averageLine = new LimitLine(yaverage, "Aver. " + String.format("%.1f", yaverage));
        averageLine.setLineWidth(2f);
        averageLine.enableDashedLine(10f, 10f, 0f);
        averageLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        averageLine.setLineColor(myContext.getResources().getColor(R.color.colorTextPrimary));
        averageLine.setTextColor(myContext.getResources().getColor(R.color.colorTextPrimary));
        averageLine.setTextSize(12f);

        YAxis yAxis = forecastChart.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(averageLine);
        yAxis.setAxisMaximum(ymax);
        yAxis.setAxisMinimum(ymin);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        yAxis.setDrawLimitLinesBehindData(false);

        forecastChart.getAxisRight().setEnabled(false);
        setData(entries, forecastChart);
    }


    public void setData(List<Entry> values, LineChart forecastChart) {
        LineDataSet set;

        if (forecastChart.getData() != null &&
                forecastChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) forecastChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            forecastChart.getData().notifyDataChanged();
            forecastChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(values, "Temp max");
            set.setDrawIcons(false);
            set.enableDashedLine(10f, 5f, 0f);
            set.enableDashedHighlightLine(10f, 5f, 0f);
            set.setColor(Color.DKGRAY);
            set.setCircleColor(Color.DKGRAY);
            set.setLineWidth(2f);
            set.setCircleRadius(3f);
            set.setDrawCircleHole(false);
            set.setValueTextSize(0f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData data = new LineData(dataSets);
            forecastChart.setData(data);
            forecastChart.setTouchEnabled(true);

            TemperatureMarkerView markerView = new TemperatureMarkerView(myContext, R.layout.forecast_weather_markerview);
            forecastChart.setMarker(markerView);


        }
    }

    @Override
    public int getItemCount()
    {
        return myCityList.size();
    }




    public class WeatherScreenCityCardHolder extends RecyclerView.ViewHolder {

        // Current information:
        public TextView cityName;
        public TextView coordinatesCity;
        public ImageView locationTypeDrawable;

        public TextView weatherDescription;
        public TextView currentSymbolWeather;


        public TextView thermometerWeather;
        public TextView currentDegrees;
        public TextView currentMaxDegrees;
        public TextView currentMinDegrees;
        public TextView currentFeelDegrees;

        public TextView sunrise;
        public TextView sunset;
        public TextView clouds;
        public TextView visibility;
        public TextView pressure;
        public TextView humidity;
        public TextView windSpeed;
        public TextView windDeg;
        public LineChart forecastChart_max;
        public LineChart forecastChart_min;

        public ImageView removeCity;



        // Link every textView label with each atribute:
        public WeatherScreenCityCardHolder(@NonNull View itemView) {
            super(itemView);

            forecastChart_max = itemView.findViewById(R.id.forecast_chart_max);
            forecastChart_min = itemView.findViewById(R.id.forecast_chart_min);


            // Current information:
            cityName = (TextView) itemView.findViewById(R.id.city_text_weather_screen);
            coordinatesCity = (TextView) itemView.findViewById(R.id.coordinates_text_weather_screen);
            locationTypeDrawable = (ImageView) itemView.findViewById(R.id.city_location_type_weather_screen);

            weatherDescription = (TextView) itemView.findViewById(R.id.weather_description_weather_screen);
            currentSymbolWeather = (TextView) itemView.findViewById(R.id.current_weather_icon_weather_screen);


            thermometerWeather = (TextView) itemView.findViewById(R.id.current_temperature_icon_weather_screen);
            currentDegrees = (TextView) itemView.findViewById(R.id.current_temperature_weather_screen);
            currentMaxDegrees = (TextView) itemView.findViewById(R.id.max_temperature_weather_screen);
            currentMinDegrees = (TextView) itemView.findViewById(R.id.min_temperature_weather_screen);
            currentFeelDegrees = (TextView) itemView.findViewById(R.id.feels_temperature_weather_screen);

            sunrise = (TextView) itemView.findViewById(R.id.sunrise_weather_screen);
            sunset = (TextView) itemView.findViewById(R.id.sunset_weather_screen);
            clouds = (TextView) itemView.findViewById(R.id.clouds_weather_screen);
            visibility = (TextView) itemView.findViewById(R.id.visibility_weather_screen);
            pressure = (TextView) itemView.findViewById(R.id.pressure_weather_screen);
            humidity = (TextView) itemView.findViewById(R.id.humidity_weather_screen);
            windSpeed = (TextView) itemView.findViewById(R.id.wind_speed_weather_screen);
            windDeg = (TextView) itemView.findViewById(R.id.wind_deg_weather_screen);

            // Typeface symbols to show correct value depending on its name (linked in string.xml):
            Typeface font = Typeface.createFromAsset(myContext.getAssets(), myContext.getString(R.string.iconFontPath));
            currentSymbolWeather.setTypeface(font);
            thermometerWeather.setTypeface(font);


            removeCity = itemView.findViewById(R.id.remove_city_weather);
        }
    }

    private String unixToDate(String unix_timestamp) {
        long timestamp = Long.parseLong(unix_timestamp) * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String date = simpleDateFormat.format(timestamp);

        return date.toString();
    }
}
