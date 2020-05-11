package com.appclima.appclimanavigation.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ManagePreferences {


    /* Information saved:
        - User information (name)
        - Array with cities names
        - Array with cities type (2: default, 3: favourite)
        - Events?
        - Login in the first activity?
     */

    // Access mode will be set using a int value from outside class
    // MODE_PRIVATE -> 3
    // MODE_WORLD_READABLE -> 2
    // MODE_WORLD_WRITEABLE -> 1
    // MODE_MULTI_PROCESS -> 0


    // Atributes:
    private String namePreferenceFile;
    private String preferenceID;
    private String preferenceValue;
    private int accessModeID;
    private Context myContext;
    SharedPreferences myPreferences;



    // Constructor (all atributes required)
    public ManagePreferences(Context myContext) {
        this.myContext = myContext;
    }

    // Get preferences:



    // Save preferences:
    public void savePreferences(String namePreferenceFile, String preferenceID, String preferenceValue, int accessModeID) {

        // Set access mode according to the ID:

        int accessMode;

        switch (accessModeID) {
            case 0:
                accessMode = myContext.MODE_MULTI_PROCESS;
                Log.d("Shared Preferences", "Mode multi-process");
                break;

            case 1:
                accessMode = myContext.MODE_WORLD_WRITEABLE;
                Log.d("Shared Preferences", "Mode world writeable");
                break;

            case 2:
                accessMode = myContext.MODE_WORLD_READABLE;
                Log.d("Shared Preferences", "Mode world readable");
                break;

            default:
                Log.d("Shared Preferences", "Mode private");
                accessMode = myContext.MODE_PRIVATE;
                break;

        }

        // As input atribute is String, it's not necessary to parse data.

        // Access to the file:
        myPreferences =  myContext.getSharedPreferences(namePreferenceFile, myContext.MODE_PRIVATE);

        // Create editor:
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString(preferenceID, preferenceValue);
        editor.commit();
    }

    public String getPreferences(String namePreferenceFile, String preferenceID) {

        // As input atribute is String, it's not necessary to parse data.

        // Access to the file:
        myPreferences =  myContext.getSharedPreferences(namePreferenceFile, myContext.MODE_PRIVATE);

        // Returns null if there is no data saved:
        String valueSaved = myPreferences.getString(preferenceID, null);

        return valueSaved;
    }
}
