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
import java.util.ArrayList;
import java.util.List;

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



    // Constant needed:
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
                    Toast.makeText(myContext, "Unable to proceed with your request. Please, try again.", Toast.LENGTH_LONG).show();
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


                return false;
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
                    temp_max_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("temp_min"));
                    temp_min_forecast.add(dayListArray.getJSONObject(i).getJSONObject("main").getDouble("temp_max"));
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
    public void currentWeatherInformation(){

        // Get JSONObject for current weather:
        JSONObject weatherDataObject = getWeatherInformationFromApi(CURRENT_WEATHER_CITY_NAME);

        // If validCity flag was set true in getWeatherInformationFromAPI, it means the city was correctly found, so we can parse data:
        if (validRequest) {
            try {

                // Parse data to extract important information from JSON:
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
                sunrise = sysInfo.getInt("sunrise");
                sunset = sysInfo.getInt("sunset");
                country = sysInfo.getString("country");
                Log.d("Sun info API", "Sunrise: " + sunrise + " Sunset: " + sunset);
                Log.d("Country: ", country);

            }

            catch (JSONException e)

            {
                e.printStackTrace();
            }
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


    public Cities getMyCityObject() {
        return myCityObject;
    }

    public ForecastCity getMyForecastCity() {
        return myForecastCity;
    }

}

