package com.appclima.appclimanavigation.presentation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.control.ManageLocation;
import com.appclima.appclimanavigation.control.ManagePreferences;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserFragment extends Fragment {


    ImageView username_edit;
    ImageView default_location_edit;
    ImageView temperature_unit_edit;
    ImageView wind_unit_edit;
    ImageView day_of_week_calendar_edit;
    Switch switch_location_updates;
    Switch switch_theme;
    Switch switch_umbrella_reminder;
    TextView username_text;
    TextView default_city_name;
    TextView temperature_unit_name;
    TextView wind_unit_name;
    TextView theme_name;
    TextView day_of_week_name;
    TextView coordinates_text;






    private Integer switchType;
    private String titleDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View userView = inflater.inflate(R.layout.fragment_user, container, false);


        // Information voice commands


        // All Buttons:
        username_edit = userView.findViewById(R.id.username_edit);
        default_location_edit = userView.findViewById(R.id.default_location_edit);
        temperature_unit_edit = userView.findViewById(R.id.temperature_unit_edit);
        wind_unit_edit = userView.findViewById(R.id.wind_unit_edit);
        day_of_week_calendar_edit = userView.findViewById(R.id.day_of_week_calendar_edit);

        // All switchs:
        switch_location_updates = userView.findViewById(R.id.switch_location_updates);
        switch_theme = userView.findViewById(R.id.switch_theme);
        switch_umbrella_reminder = userView.findViewById(R.id.switch_umbrella_reminder);

        // Texts:
        username_text = userView.findViewById(R.id.username_text);
        default_city_name = userView.findViewById(R.id.default_city_name);
        temperature_unit_name = userView.findViewById(R.id.temperature_unit_name);
        wind_unit_name = userView.findViewById(R.id.wind_unit_name);
        theme_name = userView.findViewById(R.id.theme_name);
        day_of_week_name = userView.findViewById(R.id.day_of_week_name);
        coordinates_text = userView.findViewById(R.id.coordinates_text);

        // Change texts:
        setTextsFromPreferenceFile();


        // Buttons listeners:
        switchListButton(0, "Choose Temperature Unit", new String[] {"C", "F", "K"},
                        temperature_unit_edit, R.drawable.ic_sun_pref_menu);
        switchListButton(1, "Choose Wind Unit", new String[] {"km/h", "m/s"},
                        wind_unit_edit, R.drawable.ic_wind_unit_pref_menu);
        switchListButton(2, "Choose Calendar Start of Week",
                        new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"},
                        day_of_week_calendar_edit, R.drawable.ic_calendar_day_pref_menu);

        // Entering texts:
        enterTextButton(0, username_edit, "Choose your username", R.drawable.ic_username_pref_menu);
        enterTextButton(1, default_location_edit, "Choose your default location", R.drawable.ic_default_location_pref_menu);

        // Switch changes:
        changeSwitch(switch_location_updates, 0);
        changeSwitch(switch_theme, 1);
        changeSwitch(switch_umbrella_reminder, 2);



        return userView;
    }


    private void changeSwitch(Switch listenerSwitch, final Integer switchType) {

        listenerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ManagePreferences managePreferences = new ManagePreferences(getContext());
                if(isChecked) {
                    // location updates
                    if (switchType == 0) {
                        managePreferences.setLocationUpdates(true);
                    }

                    // dark theme

                    if (switchType == 1) {
                        managePreferences.setDarkModeTheme(true);
                    }

                    // umbrella reminder
                    if (switchType == 2) {
                        managePreferences.setUmbrellaReminder(true);
                    }
                }

                else {
                    // location updates
                    if (switchType == 0) {
                        managePreferences.setLocationUpdates(false);
                    }

                    // dark theme

                    if (switchType == 1) {
                        managePreferences.setDarkModeTheme(false);
                    }

                    // umbrella reminder
                    if (switchType == 2) {
                        managePreferences.setUmbrellaReminder(false);
                    }
                }
                setTextsFromPreferenceFile();
            }
        });
    }
    private void enterTextButton (Integer enterTextType, ImageView listenerButton, String titleDialog, Integer drawableID) {
        final Integer myEnterTextType = enterTextType;
        final String myTitleDialog = titleDialog;
        final Integer myDrawableId = drawableID;

        listenerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Dialog builder:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);

                builder.setTitle(myTitleDialog); // title
                builder.setIcon(myDrawableId); // icon

                // Create edit text and add it to dialog builder:
                final EditText inputText = new EditText(getContext());
                inputText.setInputType(InputType.TYPE_CLASS_TEXT); // type text
                builder.setView(inputText);
                inputText.setTextColor(getResources().getColor(R.color.colorPrimaryText)); // color text


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManagePreferences managePreferences;
                        // Positive button --> Save preferences
                        if(myEnterTextType == 0) {
                            System.out.println("Enter username");
                            managePreferences = new ManagePreferences(getContext());
                            managePreferences.setUserName(inputText.getText().toString());
                        }

                        else if (myEnterTextType == 1) {

                            System.out.println("Enter default Location");
                            managePreferences = new ManagePreferences(getContext());
                            managePreferences.changeLocation(inputText.getText().toString(), 2);
                        }

                        dialog.dismiss();
                        setTextsFromPreferenceFile(); // Update menu
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Create dialog:
                AlertDialog usernameDialog = builder.create();

                // Showing the keyboard to input text:
                usernameDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                // Show dialog and modify styles:
                usernameDialog.show();
                usernameDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                usernameDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });


    }

    private void switchListButton(final Integer switchType, String titleDialog, String [] dialogList, ImageView listenerButton, Integer draweableID) {
        final String myTitleDialog = titleDialog;
        final Integer mySwitchType = switchType;
        final String[] myDialogList = dialogList;
        final Integer myDrawableID = draweableID;


        listenerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MaterialThemeDialog);
                builder.setTitle(myTitleDialog);
                builder.setIcon(myDrawableID);


                ManagePreferences managePreferences = new ManagePreferences(getContext());
                Integer checkedItem = -1;
                String defaultUnitSelected = "";

                //  Showed list and icon depending on switch type

                // Temperature unit
                if (switchType == 0) {
                    defaultUnitSelected = managePreferences.getDefaultUnitTemperature();
                }

                // Wind unit
                else if (switchType == 1) {
                    defaultUnitSelected = managePreferences.getDefaultUnitWind();

                }

                // Calendar start of week
                else if (switchType == 2) {
                    defaultUnitSelected = managePreferences.getDayOfWeek();

                }

                for (int i = 0; i<(myDialogList.length);i++) {
                    if(defaultUnitSelected.contains(myDialogList[i])) {
                        checkedItem = i;
                    }
                }

                // In case an item is selected:
                builder.setSingleChoiceItems(myDialogList, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ManagePreferences managePreferences = new ManagePreferences(getContext());

                        // Save preferences depending on switch type:

                        // Temperature unit
                        if (switchType == 0) {
                            managePreferences.setDefaultUnitTemperature(myDialogList[i]);
                        }

                        // Wind unit
                        else if (switchType == 1) {
                            managePreferences.setDefaultUnitWind(myDialogList[i]);

                        }

                        // Calendar start of week
                        else if (switchType == 2) {
                            managePreferences.setDayOfWeek(myDialogList[i]);

                        }

                        dialog.dismiss();
                        setTextsFromPreferenceFile();
                    }
                });

                // In case cancel button is clicked (no changes applied):
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });


                // Create dialog:
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });
    }

    private void setTextsFromPreferenceFile() {

        ManagePreferences managePreferences = new ManagePreferences(getContext());

        username_text.setText(managePreferences.getUserName());
        default_city_name.setText(managePreferences.getDefaultLocation());
        temperature_unit_name.setText(managePreferences.getDefaultUnitTemperature());
        wind_unit_name.setText(managePreferences.getDefaultUnitWind());
        day_of_week_name.setText(managePreferences.getDayOfWeek());
        coordinates_text.setText(((MainActivity)getActivity()).getCoordinates()); // Get coordinates from locationService

        // Switch buttons

        if (managePreferences.getDarkModeTheme().contains("true")) {
            switch_theme.setChecked(true);
            theme_name.setText("Dark mode enable");
        }
        else {
            switch_theme.setChecked(false);
            theme_name.setText("Dark mode disable");
        }

        if (managePreferences.getLocationUpdates().contains("true")) {
            switch_location_updates.setChecked(true);
            ((MainActivity)getActivity()).restartLocationUpdates();
        }
        else {
            switch_location_updates.setChecked(false);
            ((MainActivity)getActivity()).pauseLocationUpdates();

        }

        if (managePreferences.getUmbrellaReminder().contains("true")) {
            switch_umbrella_reminder.setChecked(true);
        }
        else {
            switch_umbrella_reminder.setChecked(false);
        }


    }

}