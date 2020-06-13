package com.appclima.appclimanavigation.presentation.cardviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import com.appclima.appclimanavigation.model.MyPrefs;

import java.text.SimpleDateFormat;
import java.util.List;
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


    public WeatherScreenCityCard(Context context, List<Cities> city, List<ForecastCity> cityForecast) {
        Log.d("WeatherScreenCityCard", " passing values");
        this.myContext = context;
        this.myCityList = city;
        this.myCityForecastList = cityForecast;

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


        // Link every textView label with each atribute:
        public WeatherScreenCityCardHolder(@NonNull View itemView) {
            super(itemView);

            System.out.println("entro aqui");

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
