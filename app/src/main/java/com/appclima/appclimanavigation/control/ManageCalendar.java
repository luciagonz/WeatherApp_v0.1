package com.appclima.appclimanavigation.control;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ManageCalendar {

    private Context myContext;
    private Activity myActivity;
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final String gmailAccount = "gonzalezsobrinolucia@gmail.com";

    private ArrayList<String> nameOfEvent;
    private ArrayList<String> startDates;
    private ArrayList<String> endDates;
    private ArrayList<String> descriptions;
    private ArrayList<Integer> calendarID;
    private ArrayList<String> eventLocation;
    private ArrayList<Boolean> allDayFlagEvent;
    private ArrayList<Integer> eventColor;
    private ArrayList<String> weatherDescriptionEvent;
    private ArrayList<Integer> eventID;
    private ArrayList<String> calendarName;


    private  ArrayList<Integer> calendarListID;
    private  ArrayList<String> calendarListAccount;
    private  ArrayList<String> calendarListName;
    private  ArrayList<Integer> calendarListColor;



    public ManageCalendar(Context myContext, Activity myActivity) {
        this.myContext = myContext;
        this.myActivity = myActivity;
    }

    // TO PRACTISE: This calendarInformation query is made using Manifest constants to query and getCalendarEvents is made using its names.
    // More information about the difference:
    // https://developer.android.com/reference/android/provider/CalendarContract.Calendars
    // https://developer.android.com/reference/android/provider/CalendarContract.Events



    public void calendarInformation() {

        calendarListID = new ArrayList<>();
        calendarListAccount = new ArrayList<>();
        calendarListName = new ArrayList<>();
        calendarListColor = new ArrayList<>();

        Cursor cursor = null;
        ContentResolver cr = myActivity.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        // Get all gmail accounts
        String selection = "(" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?)";
        String[] selectionArgs = new String[]{"com.google"};

        // If permissions read permission is not granted, ask for it:
        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ManagePermissions managePermissions = new ManagePermissions(myActivity, myContext);
            managePermissions.permissionManager(MainActivity.READ_CALENDAR_PERMISSION, MainActivity.READ_CALENDAR_PERMISSION_REQUEST_CODE);

            return;
        }

        // construct query parameters asked:
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.CALENDAR_COLOR            // 3
        };

        // Make query
        cursor = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);


        // Use the cursor to step through the returned records
        while (cursor.moveToNext()) {

            // Get the field values and add them to arrays:
            calendarListID.add((int)(cursor.getLong(0)));
            calendarListAccount.add(cursor.getString(1));
            calendarListName.add(cursor.getString(2));
            calendarListColor.add(0xff000000 + cursor.getInt(3));

        }


    }

    public CalendarEvents getCalendarEvent(String month_day_year_start, String month_day_year_end) throws ParseException {

        // Create arrays to save the event list:
        nameOfEvent = new ArrayList<>();
        startDates = new ArrayList<>();
        endDates = new ArrayList<>();
        descriptions = new ArrayList<>();
        calendarID = new ArrayList<>();
        eventLocation = new ArrayList<>();
        allDayFlagEvent = new ArrayList<>();
        eventColor = new ArrayList<>();
        weatherDescriptionEvent = new ArrayList<>();
        eventID = new ArrayList<>();
        calendarName = new ArrayList<>();

        // Get calendar information:
        calendarInformation();

        // day format: "MM/dd/yy"

        String dtstart = "dtstart";
        String dtend = "dtend";


        // Format input date to build the query
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss MM/dd/yy");

        Calendar beginOfDay = Calendar.getInstance();
        Date date_start = formatter.parse("00:00:00 " + month_day_year_start);
        beginOfDay.setTime(date_start);
       // beginOfDay.add(Calendar.HOUR, -2);
        System.out.println("start date " + date_start);


        Calendar endOfDay = Calendar.getInstance();
        Date date_end = formatter.parse("23:59:59 " + month_day_year_end);
        System.out.println("end date " + date_end);
        endOfDay.setTime(date_end);



        System.out.println("Events for today from " + beginOfDay.getTimeInMillis() + " until " + endOfDay.getTimeInMillis());

        // Query
        Cursor cursor_day_events = myContext.getContentResolver()
                    .query(Uri.parse("content://com.android.calendar/events"),
                            new String[] { "calendar_id", "title", "description", "dtstart", "dtend","eventTimezone", "eventLocation", "allDay", "_id"},
                            "( dtstart >=" + beginOfDay.getTimeInMillis() + " and dtstart <=" + endOfDay.getTimeInMillis() + ")",
                            null,
                            "dtstart ASC");



        cursor_day_events.moveToFirst();

        // fetching calendars name
        String CNames[] = new String[cursor_day_events.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        calendarID.clear();
        eventLocation.clear();
        allDayFlagEvent.clear();
        eventColor.clear();
        weatherDescriptionEvent.clear();
        eventID.clear();
        calendarName.clear();

        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor_day_events.getString(1));
            descriptions.add(cursor_day_events.getString(2));
            startDates.add(getDate(cursor_day_events.getLong(3), "dd/MM/yyyy HH:mm:ss a"));
            endDates.add(getDate(cursor_day_events.getLong(4), "dd/MM/yyyy HH:mm:ss a"));
            calendarID.add((int)cursor_day_events.getLong(0));
            eventID.add((int)cursor_day_events.getLong(8));


            for (int j = 0; j < calendarListID.size(); j++) {

                if (calendarID.get(i).equals(calendarListID.get(j))) {
                    eventColor.add(calendarListColor.get(j));
                    calendarName.add(calendarListAccount.get(j).split("@")[0]);
                }
            }

            eventLocation.add(cursor_day_events.getString(6));

            String city = cursor_day_events.getString(6).split(",")[0];

            if (!city.isEmpty()) {
                APIWeather apiWeather = new APIWeather(city, 3, myContext);
                apiWeather.manageInformationRequest(true);
                weatherDescriptionEvent.add(apiWeather.getCurrentTimeDescription());

            }

            else {
                System.out.println("Event without Location");
                weatherDescriptionEvent.add("");
            }




            // all_day = true (1), all_day = false (0)
            if (cursor_day_events.getString(7).contains("1")) { allDayFlagEvent.add(true); }

                else { allDayFlagEvent.add(false); }

            cursor_day_events.moveToNext();

        }




        CalendarEvents calendarEvents = new CalendarEvents(nameOfEvent,
                startDates, endDates, descriptions,
                calendarID, eventLocation, allDayFlagEvent, eventColor, weatherDescriptionEvent, eventID, calendarName);


        return calendarEvents;



    }

    public static void deleteEvent(int eventID) {
        System.out.println("Delete event " + eventID);
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




}

