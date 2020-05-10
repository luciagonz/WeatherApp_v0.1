package com.appclima.appclimanavigation.presentation.cardviews;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;
import com.appclima.appclimanavigation.model.CalendarEvents;
import com.appclima.appclimanavigation.model.Chat;

import java.util.List;

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

        TextView textMessage;

        public ChatMessageCardHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = (TextView) itemView.findViewById(R.id.chat_message_text_received);
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
        holder.textMessage.setText(myChat.getMessageDetected());

    }

    @Override
    public int getItemCount() {

        return myChatList.size();
    }



}

