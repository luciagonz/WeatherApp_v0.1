package com.appclima.appclimanavigation.control;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

/* This class controls SpeechReconition from an activity (activity and context must be set):
    - When the button is pushed, it creates a new intent to recognise the speech and also a textToSpeech in order to give response
    to the request.
    - This method ensures voice permission is granted.
    - It doesn't treat voice commands depending on recognised speech (that task is done in Voice Commands class), but gets the result
    and replay to the user by voice, and also changing the fragment showed.

 */

public class SpeechRecognition extends MainActivity {


    private Activity myActivity;
    private Context myContext;
    private VoiceCommands recognisedSpeechReplay; // Selected speech from the array possibilites given by VoiceCommands
    private TextToSpeech voiceCommand; // Replay to the speech given by VoiceCommands class
    private ManagePermissions audioPermission; // Object to manage audioPermissions
    private String textReplayed;
    private String textRecognised;
    private boolean wellRecognised;


    // Activity and context must be set in the constructor.
    public SpeechRecognition(Activity myActivity, Context myContext) {
        this.myActivity = myActivity;
        this.myContext = myContext;
    }


    // When microphone button is pushed:
    public void speechRequest (View view, VoiceCommands voiceCommands) {

        this.recognisedSpeechReplay = voiceCommands;
        // Ensures audio permission is granted:
        audioPermission = new ManagePermissions(myActivity, myContext);
        audioPermission.setPermissionRequest(AUDIO_PERMISSION);
        audioPermission.setRequestCodePermission(AUDIO_PERMISSION_REQUEST_CODE);
        audioPermission.permissionManager();

        System.out.println("audio permission is enabled? " + audioPermission.isPermissionEnabled());

        // SPEECH RECOGNITION SETTINGS
        // If audio permission is granted:
        if (audioPermission.isPermissionEnabled() == true) {

            // Creates intent to recognise speech
            Intent speechIntent;
            speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            // Set language to english:
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.ENGLISH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, myContext.getPackageName());

            System.out.println("intent: " + speechIntent);

            // speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.starting_recognition));

            // Starts recognition:
            try {
                System.out.println("try onActivityResult"); // Debug purpose
                myActivity.startActivityForResult(speechIntent, 1);
            }

            // Exception in case is not possible to start intent:
            catch (ActivityNotFoundException exception) {
                Log.d("LOG", exception.getMessage());
            }

            // SPEECH REPLAY SETTINGS

            // Creates class TextToSpeech in order to replay to the user by voice:
            voiceCommand = new TextToSpeech(myActivity.getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {

                    // If initialization succeed:
                    if(status != TextToSpeech.ERROR) {
                        // Set language to text to speech:
                        int result = voiceCommand.setLanguage(Locale.UK);
                        // Check if language is available:
                        if ((result == TextToSpeech.LANG_MISSING_DATA) || (result == TextToSpeech.LANG_NOT_SUPPORTED)) {
                            Log.d("LOG", "Language not supported");
                        }
                    }

                    // If initialization failed:
                    else {
                        Log.d("LOG", "Initialization failed");
                    }
                }
            });
        }

        // If audio permission is not granted, request it to the user:
        else {
            audioPermission.permissionManager();
        }
    }


    // This method handles when speech recogniser intent is on:
    public void HandlerOnActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Manage voice commands
        switch (requestCode) {

            case 1:
                // Print voice recognition
                if (null != data) {

                    System.out.println("voice recognition"); // Debug purpose

                    // Get recognised Text
                    ArrayList<String> recognisedText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    System.out.println(recognisedText);

                    // Call method to manage replay
                    recognisedSpeechReplay.manageReplayAction(recognisedText);

                    // Get fragment displayed and voice text command:
                    String fragmentDisplayed = recognisedSpeechReplay.getFragmentDisplayed();
                    System.out.println("Fragment Displayed " + fragmentDisplayed);



                    // Take values from replay and recognisition from Voice Command class in order to print it in the chat:
                    textReplayed = recognisedSpeechReplay.getSpeechReplayed();
                    System.out.println("Text replay: " + textReplayed);

                    textRecognised = recognisedSpeechReplay.getSpeechRecognisedAsTrue();
                    System.out.println(textRecognised);

                    wellRecognised = recognisedSpeechReplay.isSpeechRecognised();
                    System.out.println("Well recognised?" + wellRecognised);

                    // Create Text to Speech in order to give the response:
                    voiceCommand.speak(textReplayed, TextToSpeech.QUEUE_ADD, null);

                    // Change fragment displayed:
                    selectBottomNavigationOption(fragmentDisplayed);

                }
                break;
        }
    }

    // Change navigation item according to its name
    public void selectBottomNavigationOption(String selectedItem) {

        // Find navigation by id:
        BottomNavigationView myBottomNavigationView = myActivity.findViewById(R.id.nav_view);
        System.out.println(myBottomNavigationView);
        // Defines default item id:
        Integer item = null;
        System.out.println(selectedItem);

        // Depending on string input, displays the screen:
        switch (selectedItem) {
            case "home":
                item = R.id.navigation_home;
                break;
            case "location":
                item = R.id.navigation_location;
                break;
            case "calendar":
                item = R.id.navigation_calendar;
                break;
            case "user":
                item = R.id.navigation_user;
                break;
            default:
                item = R.id.navigation_voice;
        }
        myBottomNavigationView.setSelectedItemId(item);
    }

    public String getTextReplayed() {
        return textReplayed;
    }

    public void setTextReplayed(String textReplayed) {
        this.textReplayed = textReplayed;
    }

    public String getTextRecognised() {
        return textRecognised;
    }

    public void setTextRecognised(String textRecognised) {
        this.textRecognised = textRecognised;
    }

    public boolean isWellRecognised() {
        return wellRecognised;
    }

    public void setWellRecognised(boolean wellRecognised) {
        this.wellRecognised = wellRecognised;
    }
}
