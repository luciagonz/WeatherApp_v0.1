package com.appclima.appclimanavigation.control;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.appclima.appclimanavigation.model.Cities;
import com.appclima.appclimanavigation.model.ForecastCity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIWeather extends Activity {

    // City current info:
    private double longitude;
    private double latitude;

    private String weatherMainInfo;
    private String weatherDescription;

    private double currentTemperature;
    private double maxTemperature;
    private double minTemperature;
    private double feelsTemperature;

    private int pressure;
    private int humidity;
    private int visibility;

    private double windSpeed;
    private double windDegrees;
    private int clouds;

    private int sunrise;
    private int sunset;

    private int weatherIconID;
    private String country;
    private Integer locationType;
    private Cities myCityObject;
    private ForecastCity myForecastCity;

    // City forecast info:
    List<Integer> time;
    List<String> time_text;
    List<Double> temp_forecast;
    List<Double> temp_feels_like_forecast;
    List<Double> temp_max_forecast;
    List<Double> temp_min_forecast;
    List<Integer> pressure_forecast;
    List<Integer> sea_level_forecast;
    List<Integer> ground_level_forecast;
    List<Integer> humidity_forecast;
    List<String>  weatherMainInfo_forecast;
    List<String>  weatherDescription_forecast;
    List<Integer>  weatherIconID_forecast;
    List<Integer>  clouds_forecast;
    List<Integer>  wind_speed_forecast;
    List<Integer>  degree_wind_forecast;
    List<String>  city_name_forecast;

    // Constants needed:
    private final String API_KEY = "40f93e8b2c5c0dc897458467878b8b6b";
    private final String BASE_URL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final String BASE_URL_FORECAST_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?q=";

    // Request type:
    private final int CURRENT_WEATHER_CITY_NAME = 0;
    private final int FORECAST_WEATHER_CITY_NAME = 1;

    URL urlRequest;
    Context myContext;
    boolean connectionWorking;
    private String cityName;
    private boolean validRequest = false;


    // Constructor:
    public APIWeather(String cityName, Integer locationType, Context myContext) {
        this.cityName = cityName;
        this.locationType = locationType; // to know if its fav, GPS or default
        this.myContext = myContext;
    }

    public APIWeather(String cityName, Context myContext) {
        this.cityName = cityName;
        this.locationType = 3; // to know if its fav, GPS or default
        this.myContext = myContext;
    }

    public APIWeather(Context myContext) {
        this.myContext = myContext;
    }



    public boolean manageInformationRequest(boolean isCityValidated) {

        // INPUT MEANING
        // If the request comes from validated source: isCityValidated = true,
        // if comes from input speech: isCityValidated = false;

        // STEP 1: CHECK CONNECTION
        // Check for internet connection:
        connectionWorking = checkInternetConnection();

        // If connection is not working fine:
        if(!connectionWorking) {
            Toast.makeText(myContext, "Internet not available. Please, check your connectivity and try again", Toast.LENGTH_LONG).show();
            return false;
        }

        // If connection is working fine:
        else {

            // STEP 2A: IF CITY NAME IS VALIDATED ALREADY (comes from preferences or manually written, so  input string is exactly the name of the city, no changes needed):
            if (isCityValidated) {
                // If API request worked fine, add information to ArrayListCurrentWeather:
                currentWeatherInformation();
                forecastWeatherInformation();


                if (validRequest) {

                    // 1 PART: CURRENT WEATHER
                    myCityObject = new Cities(longitude,latitude, weatherMainInfo, weatherDescription,
                            currentTemperature, maxTemperature, minTemperature, feelsTemperature,
                            pressure, humidity, visibility, windSpeed, windDegrees, clouds, sunrise,
                            sunset, cityName, country, weatherIconID, locationType);

                    // 2 PART: FORECAST WEATHER
                    myForecastCity = new ForecastCity(time, time_text, temp_forecast, temp_feels_like_forecast, temp_max_forecast, temp_min_forecast,
                            pressure_forecast, sea_level_forecast, ground_level_forecast, humidity_forecast, weatherMainInfo_forecast,
                            weatherDescription_forecast, weatherIconID_forecast, clouds_forecast, wind_speed_forecast, degree_wind_forecast, city_name_forecast);
                    return true;
                }

                else {
                    return false;
                }
            }

            // STEP 2B: IF CITY NAME IS NOT VALIDATED (comes from input speech):
            /* TODO: If request comes from input speech, input string are all the words after "in" keyword, so it's necessary to request first word,
                and if the response is null, then ask for two words information.
            */

            else {

                /* TODO: Make iteration with all words listened (For instance: New, New York, New York City,...) and
                    stop iterating when validRequest = true (API returns correct values) or when no more word were found
                 */


                // If API request worked fine, add information to ArrayListCurrentWeather:
                validRequest = currentWeatherInformation();


                if (validRequest) {

                    System.out.println("Valid request, create city: ");

                    forecastWeatherInformation();
                    // 1 PART: CURRENT WEATHER
                    myCityObject = new Cities(longitude,latitude, weatherMainInfo, weatherDescription,
                            currentTemperature, maxTemperature, minTemperature, feelsTemperature,
                            pressure, humidity, visibility, windSpeed, windDegrees, clouds, sunrise,
                            sunset, cityName, country, weatherIconID, locationType);

                    // 2 PART: FORECAST WEATHER
                    myForecastCity = new ForecastCity(time, time_text, temp_forecast, temp_feels_like_forecast, temp_max_forecast, temp_min_forecast,
                            pressure_forecast, sea_level_forecast, ground_level_forecast, humidity_forecast, weatherMainInfo_forecast,
                            weatherDescription_forecast, weatherIconID_forecast, clouds_forecast, wind_speed_forecast, degree_wind_forecast, city_name_forecast);
                    return true;
                }

                else {

                    System.out.println("Request error, city not found");

                    return false;
                }


            }
        }
    }

    // It returns integer symbol by name saved on string.xml file:
    private Integer findSymbolByName(String weatherSymbolName) {

        String packageName = myContext.getPackageName();

        Integer identifierSymbol = myContext.getResources().getIdentifier(weatherSymbolName, "string", packageName);

        return identifierSymbol;
    }



    // Check internet connection
    public boolean checkInternetConnection() {

        // Check connection and returns boolean if the device is well connected to internet:
        ConnectivityManager connectivityManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // Parse weather forecast information from openweathermap.com
    private void forecastWeatherInformation(){
        // Get JSONObject for current weather:
        JSONObject weatherDataObject = getWeatherInformationFromApi(FORECAST_WEATHER_CITY_NAME);

        if (validRequest) {
            try {

                // Important: To clearly know how to parse JSON: https://jsonformatter.curiousconcept.com (it's easier, explain in final document!)

                JSONArray dayListArray = weatherDataObject.getJSONArray("list");
                String cityNameForecast = weatherDataObject.getJSONObject("city").getString("name");
                System.out.println("city = " + cityNameForecast);
                Log.d("Forecast array size", String.valueOf(dayListArray.length()));

                // Initialize lists:
                time = new ArrayList<>();
                time_text = new ArrayList<>();
                temp_forecast = new ArrayList<>();
                temp_feels_like_forecast = new ArrayList<>();
                temp_max_forecast = new ArrayList<>();
                temp_min_forecast = new ArrayList<>();
                pressure_forecast = new ArrayList<>();
                sea_level_forecast = new ArrayList<>();
                ground_level_forecast = new ArrayList<>();
                humidity_forecast = new ArrayList<>();
                weatherDescription_forecast = new ArrayList<>();
                weatherIconID_forecast = new ArrayList<>();
                weatherMainInfo_forecast = new ArrayList<>();
                clouds_forecast = new ArrayList<>();
                wind_speed_forecast = new ArrayList<>();
                degree_wind_forecast = new ArrayList<>();
                city_name_forecast = new ArrayList<>();


                // Enter all data in arrayList for each attribute, which contains as many registers as the original array does.
                for (int i = 0; i < dayListArray.length(); i++) {
                    time.add(dayListArray.getJSONObject(i).getInt("dt"));
                    time_text.add(dayListArray.getJSONObject(i).getString("dt_txt"));
                    temp_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("temp"));
                    temp_feels_like_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("feels_like"));
                    temp_max_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("temp_max"));
                    temp_min_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("temp_min"));
                    pressure_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getInt("pressure"));
                    sea_level_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getInt("sea_level"));
                    ground_level_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getInt("grnd_level"));
                    humidity_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getInt("humidity"));
                    weatherDescription_forecast.add(dayListArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                    weatherIconID_forecast.add(getSymbolIdFromFigureName(dayListArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")));
                    weatherMainInfo_forecast.add(dayListArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                    clouds_forecast.add(dayListArray.getJSONObject(i).getJSONObject("clouds").getInt("all"));
                    wind_speed_forecast.add(dayListArray.getJSONObject(i).getJSONObject("wind").getInt("speed"));
                    degree_wind_forecast.add(dayListArray.getJSONObject(i).getJSONObject("wind").getInt("deg"));
                    city_name_forecast.add(cityNameForecast);

                }
            }

            catch (JSONException e)

            {
                e.printStackTrace();
            }
        }
    }

    // Parse current weather information from openweathermap.com
    public boolean currentWeatherInformation(){

        // Get JSONObject for current weather:
        JSONObject weatherDataObject = getWeatherInformationFromApi(CURRENT_WEATHER_CITY_NAME);

        if(validRequest) {
            // If validCity flag was set true in getWeatherInformationFromAPI, it means the city was correctly found, so we can parse data:
            try {

                // Parse data to extract important information from JSON:
                Integer timezone = weatherDataObject.getInt("timezone");
                Log.d("TimeZone API: ", timezone.toString());
                JSONObject coordinates = weatherDataObject.getJSONObject("coord");
                latitude = coordinates.getDouble("lat");
                longitude = coordinates.getDouble("lon");
                Log.d("Coordinates API:", "Lat: " + latitude + " Long: " + longitude);

                JSONArray weatherArray = weatherDataObject.getJSONArray("weather");
                JSONObject weatherInfo = weatherArray.getJSONObject(0);
                weatherMainInfo = weatherInfo.getString("main");
                weatherDescription = weatherInfo.getString("description");
                weatherIconID = getSymbolIdFromFigureName(weatherInfo.getString("icon"));
                Log.d("Weather info API", weatherMainInfo + " " + weatherDescription);

                JSONObject weatherParameters = weatherDataObject.getJSONObject("main");
                currentTemperature = weatherParameters.getDouble("temp");
                feelsTemperature = weatherParameters.getDouble("feels_like");
                minTemperature = weatherParameters.getDouble("temp_min");
                maxTemperature = weatherParameters.getDouble("temp_max");
                pressure = weatherParameters.getInt("pressure");
                humidity = weatherParameters.getInt("humidity");
                Log.d("Temperature API: ", "max: " + maxTemperature + " min: " + minTemperature + " real: " + currentTemperature + " feel-like " + feelsTemperature);
                Log.d("Other parameters API: ", "Pressure: " + pressure + " Humidity: " + humidity);

                visibility = weatherDataObject.getInt("visibility");
                Log.d("Visibility API: ", String.valueOf(visibility));

                JSONObject wind = weatherDataObject.getJSONObject("wind");
                windSpeed = wind.getDouble("speed");
                windDegrees = wind.getDouble("deg");
                Log.d("Wind info API", "Speed: " + windSpeed + " Degrees: " + windDegrees);

                JSONObject cloudsObj = weatherDataObject.getJSONObject("clouds");
                clouds = cloudsObj.getInt("all");
                Log.d("Clouds info API:", String.valueOf(clouds));

                JSONObject sysInfo = weatherDataObject.getJSONObject("sys");
                // Add timezone to show correct time depending on its location:
                sunrise = sysInfo.getInt("sunrise") + timezone;
                sunset = sysInfo.getInt("sunset") + timezone;
                country = sysInfo.getString("country");
                Log.d("Sun info API", "Sunrise: " + sunrise + " Sunset: " + sunset);
                Log.d("Country: ", country);

                return validRequest = true;

            } catch (JSONException e) {
                e.printStackTrace();
                return validRequest = false;
            }
        }

        else {
            return validRequest = false;
        }
    }



    // GET current weather information as JSON Object
    private JSONObject getWeatherInformationFromApi(int queryType) {

        // avoid error:
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            switch (queryType) {
                // Create url according to query type request (encode IOexception if fails)
                case CURRENT_WEATHER_CITY_NAME:
                    System.out.println("Current weather request, request type: " + queryType);
                    urlRequest = new URL(BASE_URL_CURRENT_WEATHER + URLEncoder.encode(cityName, "utf-8") + "&appid=" + API_KEY);
                    break;

                case FORECAST_WEATHER_CITY_NAME:
                    System.out.println("Forecast weather request" +  queryType);
                    urlRequest = new URL(BASE_URL_FORECAST_WEATHER + URLEncoder.encode(cityName, "utf-8") + "&appid=" + API_KEY);
                    break;

                default:
                    // Debug purpose:
                    urlRequest = new URL("http://api.openweathermap.org/data/2.5/weather?q=London");
            }

            Log.d("URL request: ", urlRequest.toString());

            // Connect to the service and set timeouts:
            HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Get response:
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // debug purpose:
            System.out.println(bufferedReader.toString());

            // Convert text to JSON:
            StringBuffer jsonResponse = new StringBuffer(2048);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                jsonResponse.append(line + "\n");

            }

            bufferedReader.close();
            inputStreamReader.close();
            connection.disconnect();

            // Create JSON Object:
            JSONObject weatherData = new JSONObject(jsonResponse.toString());


            // Show all information in console to check it its correct (console max output: 4000, which is not enough for forecast information)
            if (String.valueOf(weatherData).length() > 4000) {
                Log.v("Weather data API lenght", "sb.length = " + String.valueOf(weatherData).length());
                int divisions = String.valueOf(weatherData).length() / 4000;
                for (int i = 0; i <= divisions; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= String.valueOf(weatherData).length()) {
                        Log.v("Weather data", "Part " + i + " of " + divisions + ":" + String.valueOf(weatherData).substring(4000 * i));
                    } else {
                        Log.v("Weather data", "Part " + i + " of " + divisions + ":" + String.valueOf(weatherData).substring(4000 * i, max));
                    }
                }
            }

            if (weatherData == null) {

                Log.d("API Request: ", "City is not valid");

                validRequest = false;
            }

            else {

                Log.d("API Request: ", "JSON Object obtained");
                validRequest = true;
            }

            return weatherData;
        }

        catch(Throwable t) {
            Log.d("API Request: ", "Exception!");
            t.printStackTrace();
            validRequest = false;
            return null;
        }
    }

    // Manage command responses:
    public String getCurrentTimeDescription() {
        return weatherDescription;
    }

    public String getCurrentTemperature() {
        ManagePreferences managePreferences = new ManagePreferences(myContext);
        String unitTemperature = managePreferences.getDefaultUnitTemperature();
        Log.d("Temperature unit: ", "Unit is "+ unitTemperature);
        String currentTemp;

        if(unitTemperature.contains("C")) {
            currentTemp = String.format("%.2f", (currentTemperature - 273.15)) + " Celsius degrees";


        }

        else if (unitTemperature.contains("F")) {
            currentTemp =   String.format("%.2f", ((currentTemperature*9/5) - 459.67)) + " Fahrenheit degrees";
        }

        else { // Kelvin (default in API)
            currentTemp =   String.format("%.2f", currentTemperature) + " Kelvin degrees";

        }

        return currentTemp;
    }

    public String getCurrentHumidity() {
        return  humidity + "%";
    }

    public Double temperatureUnitMesure (Double absoluteTemperature) {

        ManagePreferences managePreferences = new ManagePreferences(myContext);
        String unitTemperature = managePreferences.getDefaultUnitTemperature();
        Double convertedTemperature = 0.00;


        if(unitTemperature.contains("C")) {
            convertedTemperature = (absoluteTemperature - 273.15);

        }

        else if (unitTemperature.contains("F")) {
            convertedTemperature =   (absoluteTemperature*9/5) - 459.67;
        }

        else { // Kelvin (default in API)
            convertedTemperature =   absoluteTemperature;

        }
        return convertedTemperature;
    }




   public String getAverageHumidity(Integer relativeDay) {

       Double averageHumidity = 0.00;
       Integer countHumidityIterations = 0;
       for(int i = 0; i < time_text.size(); i++) {

           if (getRelativeDayForecast(time_text.get(i)) == relativeDay) {
               averageHumidity = averageHumidity + humidity_forecast.get(i);
               countHumidityIterations = countHumidityIterations + 1;
           }

       }

       if ((averageHumidity == 0) & (relativeDay == 0)) {
           return getCurrentHumidity();
       }

       else {
           return String.format("%.1f",(averageHumidity/countHumidityIterations)) + " %";

       }

   }

   public String getMostCommonTimeDescription(Integer relativeDay) {

       List<String> timeDescriptionDay = new ArrayList<>();


       for(int i = 0; i < time_text.size(); i++) {

           if (getRelativeDayForecast(time_text.get(i)) == relativeDay) {
               timeDescriptionDay.add(weatherDescription_forecast.get(i));
           }
       }

       System.out.println(timeDescriptionDay.toString());

       // Search most popular string:
       Map<String, Integer> stringsCount = new HashMap<>();

       // Count each description iterating over the array (fill the map):

       for(String description: timeDescriptionDay)
       {
           Integer count = stringsCount.get(description);
           if(count == null) count = new Integer(0);
           count++;
           stringsCount.put(description,count);
       }

       // Find most repeated string iterating over the map
       Map.Entry<String,Integer> mostRepeated = null;
       for(Map.Entry<String, Integer> e: stringsCount.entrySet())
       {
           if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
               mostRepeated = e;
       }

       if(mostRepeated != null) {
           return mostRepeated.getKey();
       }

       else {
           return weatherDescription;
       }


   }

    public String getAverageTemperature(Integer relativeDay) {

        ManagePreferences managePreferences = new ManagePreferences(myContext);
        String unitTemperature = managePreferences.getDefaultUnitTemperature();
        Log.d("Temperature unit: ", "Unit is "+ unitTemperature);

        Double averageTemperature = 0.00;
        Integer countTemperatures = 0;

        for(int i = 0; i < time_text.size(); i++) {

            if (getRelativeDayForecast(time_text.get(i)) == relativeDay) {
                averageTemperature = averageTemperature + temp_forecast.get(i);
                countTemperatures = countTemperatures + 1;
            }

        }

        String averageTemperatureString;
        if ((averageTemperature == 0.00) & (relativeDay == 0)) {
            averageTemperatureString = getCurrentTemperature();
        }

        else {
            averageTemperature = averageTemperature/countTemperatures;
            Log.d("Average Temperature", String.valueOf(averageTemperature));
            if(unitTemperature.contains("C")) {

                averageTemperatureString = String.format("%.2f", (averageTemperature - 273.15)) + " Celsius degrees";


            }

            else if (unitTemperature.contains("F")) {
                averageTemperatureString =   String.format("%.2f", ((averageTemperature*9/5) - 459.67)) + " Fahrenheit degrees";
            }

            else { // Kelvin (default in API)
                averageTemperatureString =   String.format("%.2f", averageTemperature) + " Kelvin degrees";

            }
        }


        return averageTemperatureString;
    }

    public String getDayName(Integer relativeDay) {

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE,relativeDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekName = " ";

        switch (dayOfWeek) {
            case 1:
                dayOfWeekName = "Sunday";
                break;
            case 2:
                dayOfWeekName = "Monday";
                break;
            case 3:
                dayOfWeekName = "Tuesday";
                break;
            case 4:
                dayOfWeekName = "Wednesday";
                break;
            case 5:
                dayOfWeekName = "Thursday";
                break;
            case 6:
                dayOfWeekName = "Friday";
                break;
            case 7:
                dayOfWeekName = "Saturday";
        }

        return dayOfWeekName;
    }


    public Integer getRelativeDayForecast(String date) {

       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Date currentDate = new Date();
       String day_0 = dateFormat.format(currentDate); // today
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(currentDate);
       calendar.add(Calendar.DATE,1);
       String day_1 = dateFormat.format(calendar.getTime()); // tomorrow
       calendar.add(Calendar.DATE,1);
       String day_2 = dateFormat.format(calendar.getTime());
       calendar.add(Calendar.DATE,1);
       String day_3 = dateFormat.format(calendar.getTime());
       calendar.add(Calendar.DATE,1);
       String day_4 = dateFormat.format(calendar.getTime());
       calendar.add(Calendar.DATE,1);
       String day_5 = dateFormat.format(calendar.getTime());
       calendar.add(Calendar.DATE,1);


       Integer relativeDay;

       if (date.contains(day_0)) {
           relativeDay = 0;
       }

       else if (date.contains(day_1)) {
           relativeDay = 1;
       }

       else if (date.contains(day_2)) {
           relativeDay = 2;
       }

       else if (date.contains(day_3)) {
           relativeDay = 3;
       }

       else if (date.contains(day_4)) {
           relativeDay = 4;
       }

       else {
           relativeDay = 5;
       }

       return relativeDay;
   }


   public ArrayList<Double> averageTemperatureForecast() {
       ArrayList<Double> temperatureForecast = new ArrayList<>();
       ArrayList<Double> sumTemperatures = new ArrayList<>();
       ArrayList<Integer> countTemperatures = new ArrayList<>();

       // Array to storage averages and number of temperatures provided by API
       for (int i = 0; i < 6; i++) {
           sumTemperatures.add(0.00);
           countTemperatures.add(0);
       }

       // Get all temperatures and added to sum temperatures in each day

       for (int i = 0; i < time_text.size(); i++) {

           // Look the day (time_text.get(i)) and save the temperature added with others to the correct position of th array
           for (int j = 0; j < 6; j++) {
               if (getRelativeDayForecast(time_text.get(i)) == j) {
                   // temperature = previously_temperature + new_temperature
                   sumTemperatures.set(j, sumTemperatures.get(j) + temp_forecast.get(i));
                   // count new temperature added:
                   countTemperatures.set(j, countTemperatures.get(j) + 1);
               }
           }
       }

       for (int j = 0; j < 6; j++) {
           if ((j == 0) & (sumTemperatures.get(j) == 0)){
               temperatureForecast.add(temperatureUnitMesure(currentTemperature));
           }
           else {
               temperatureForecast.add(temperatureUnitMesure(sumTemperatures.get(j)/countTemperatures.get(j)));
           }

       }

       System.out.println(temperatureForecast.toString());
       return temperatureForecast;
   }

    // Match figures indicated on "icon" element from API with symbols from the font.
    public int getSymbolIdFromFigureName (String figureName) {
        String weatherIconName = null;

        switch (figureName) {
            case "01d":
                weatherIconName = "wi_day_sunny";
                break;

            case "02d":
                weatherIconName = "wi_day_cloudy";
                break;

            case "03d":
                weatherIconName = "wi_cloud";
                break;

            case "04d":
                weatherIconName = "wi_cloudy";
                break;

            case "09d":
                weatherIconName = "wi_showers";
                break;

            case "10d":
                weatherIconName = "wi_day_rain_mix";
                break;
            case "11d":
                weatherIconName = "wi_thunderstorm";
                break;

            case "13d":
                weatherIconName = "wi_snow";
                break;

            case "50d":
                weatherIconName = "wi_fog";
                break;

            case "01n":
                weatherIconName = "wi_night_clear";
                break;

            case "02n":
                weatherIconName = "wi_night_alt_cloudy";
                break;

            case "03n":
                weatherIconName = "wi_night_alt_cloudy_high";
                break;

            case "04n":
                weatherIconName = "wi_night_cloudy";
                break;

            case "09n":
                weatherIconName = "wi_night_alt_sprinkle";
                break;

            case "10n":
                weatherIconName = "wi_night_alt_showers";
                break;
            case "11n":
                weatherIconName = "wi_night_alt_thunderstorm";
                break;

            case "13n":
                weatherIconName = "wi_night_alt_snow";
                break;

            case "50n":
                weatherIconName = "wi_night_fog";
                break;
        }

        Integer symbolID = findSymbolByName(weatherIconName);

        return symbolID;

    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Cities getMyCityObject() {
        return myCityObject;
    }

    public ForecastCity getMyForecastCity() {
        return myForecastCity;
    }

}

