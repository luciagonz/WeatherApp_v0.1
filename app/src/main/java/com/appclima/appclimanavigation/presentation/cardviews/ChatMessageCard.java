package com.appclima.appclimanavigation.presentation.cardviews;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Chat;

import java.util.List;

import static androidx.core.content.ContextCompat.*;

/**
 * Implementation of App Widget functionality.
 */
public class ChatMessageCard extends RecyclerView.Adapter<ChatMessageCard.ChatMessageCardHolder> {

    Context context;
    List<Chat> myChatList;

    public ChatMessageCard(Context context, List<Chat> chatList) {
        this.context = context;
        this.myChatList = chatList;
    }


    // Set values to objects in the cardview:

    public class ChatMessageCardHolder extends RecyclerView.ViewHolder {

        TextView textMessageVoice;
        LinearLayout relativeLayout_voice;
        TextView textMessageUser;
        LinearLayout relativeLayout_user;
        LinearLayout linearLayout;

        public ChatMessageCardHolder(@NonNull View itemView) {
            super(itemView);
            textMessageUser = (TextView) itemView.findViewById(R.id.chat_message_text_received);
            textMessageVoice = (TextView) itemView.findViewById(R.id.chat_message_text_sent);
            relativeLayout_user = (LinearLayout) itemView.findViewById(R.id.box_message_user);
            relativeLayout_voice = (LinearLayout) itemView.findViewById(R.id.box_message_voice);
            linearLayout = itemView.findViewById(R.id.lineal_layout_chat);
        }
    }

    @NonNull

    @Override
    public ChatMessageCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // When a card is created:
        View chatCardView = LayoutInflater.from(context).inflate(R.layout.cardview_message_received, parent, false);
        return new ChatMessageCardHolder(chatCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageCardHolder holder, int position) {
        Chat myChat = myChatList.get(position);



        if (myChat.getTransmitter() == 0) {
            // holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.relativeLayout_voice.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_chat_message_sent));
            holder.relativeLayout_voice.setVisibility(View.VISIBLE);
            holder.textMessageVoice.setVisibility(View.VISIBLE);
            holder.textMessageVoice.setText(myChat.getMessageDetected());

            holder.relativeLayout_user.setVisibility(View.GONE);

            // holder.linealLayout.setPadding(5, 3, 100, 3);

            System.out.println("me");

        }

        else {
           // holder.relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            holder.relativeLayout_user.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_chat_message_received));
            holder.relativeLayout_user.setVisibility(View.VISIBLE);
            holder.textMessageUser.setText(myChat.getMessageDetected());
            holder.textMessageUser.setVisibility(View.VISIBLE);


            holder.relativeLayout_voice.setVisibility(View.GONE);

            System.out.println("voice assistant");

            //  holder.linealLayout.setPadding(100, 3, 5, 3);

        }

    }

    @Override
    public int getItemCount() {

        return myChatList.size();
    }



}