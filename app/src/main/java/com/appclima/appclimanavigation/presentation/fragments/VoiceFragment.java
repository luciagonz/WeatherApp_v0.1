package com.appclima.appclimanavigation.presentation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.Chat;
import com.appclima.appclimanavigation.presentation.activities.MainActivity;
import com.appclima.appclimanavigation.presentation.cardviews.ChatMessageCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VoiceFragment extends Fragment  {
// https://developer.android.com/guide/components/fragments


    ArrayList<Chat> chatList;
    RecyclerView recyclerChatCards;
    MainActivity myActivity;


    // Empty constructor:
    public VoiceFragment() {
        // When the fragment is created, look in which information should be appear:
        Log.d("Voice fragment", "New voice fragment created");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Chat Voice Fragment", "onCreate Method");
        // this fragments is part of main activity
        myActivity = (MainActivity) getActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Chat Voice Fragment", "New view created");

        // Set the fragment to the layout:
        final View chatView = inflater.inflate(R.layout.fragment_voice, container, false);

        initializeChat(chatView);

        // List view
        return chatView;
    }

    private void initializeChat (View myView) {

        getCardInformation();

        Log.d("Chat Voice Fragment", "Items found" + chatList.size());

        if(chatList !=null) {

            // Define linear layout manager:
            LinearLayoutManager layoutManagerChats= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            // find recycler view in home fragment by ID
            recyclerChatCards = (RecyclerView) myView.findViewById(R.id.recycler_chat_RV);

            // We need to use getContext because we are not in MainActivity but in fragment:
            recyclerChatCards.setLayoutManager(layoutManagerChats);

            // Adapt information to cardview
            ChatMessageCard chatCard = new ChatMessageCard(getContext(), chatList);

            recyclerChatCards.setAdapter(chatCard);


        }

    }

    private void getCardInformation() {
        chatList = myActivity.getVoiceMessagesArray();
    }

}



