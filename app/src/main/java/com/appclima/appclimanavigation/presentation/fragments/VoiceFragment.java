package com.appclima.appclimanavigation.presentation.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;


public class VoiceFragment extends Fragment  {

    // private VoiceViewModel voiceViewModel;
    private TextView textSpeech; //To show input Speech
    private ImageView inputSpeechButton; //To show input Speech
    private View rootView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View root = inflater.inflate(R.layout.fragment_voice, container, false);
        rootView = root;


        // Set the welcome text
        final TextView textView = rootView.findViewById(R.id.recognisedSpeech);
        textSpeech = textView;
        textSpeech.setText(R.string.recognised_speech_text);

        // Set the phone image
        final ImageView imageView = rootView.findViewById(R.id.push_to_talk_button);
        inputSpeechButton = imageView;


        return root;

    }


    public void editTextRecognisedSpeech (String textToShow) {
        textSpeech.setText(textToShow);
    }

    public void editInputSpeechButton (Drawable drawableToShow) {

        // To get drawable from project: getResources().getDrawable(R.drawable.EL_QUE_SEA)

        inputSpeechButton.setImageDrawable(drawableToShow);
    }




}



