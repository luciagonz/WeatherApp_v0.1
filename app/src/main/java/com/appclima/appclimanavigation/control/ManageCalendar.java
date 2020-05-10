package com.appclima.appclimanavigation.control;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class ManageCalendar {

    private Context myContext;
    private Activity myActivity;


    public ManageCalendar(Context myContext, Activity myActivity) {
        this.myContext = myContext;
        this.myActivity = myActivity;
    }


    public void accessToCalendar() {

    }

    public void createNewEventCalendar() {


    }


    public void getEventsFromCalendarByDay(){

    }




}

