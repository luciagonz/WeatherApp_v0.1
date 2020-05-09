package com.appclima.appclimanavigation.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appclima.appclimanavigation.R;

public class CalendarFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set the fragment view
        final View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        return root;
    }

}