package com.appclima.appclimanavigation.presentation.cardviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appclima.appclimanavigation.R;

import java.util.ArrayList;

public class FuncionalityCard extends RecyclerView.Adapter<FuncionalityCard.FunctionalityCardHolder> {

    Context myContext;
    ArrayList<String> texts;
    ArrayList<Integer> icons;


    public FuncionalityCard (Context myContext, ArrayList<String> texts, ArrayList<Integer> icons) {
        this.myContext = myContext;
        this.texts = texts;
        this.icons = icons;

    }

    @NonNull
    @Override
    public FunctionalityCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View onBoardingCard = LayoutInflater.from(myContext).inflate(R.layout.cardview_on_boarding, parent, false);

        return new FunctionalityCardHolder(onBoardingCard);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionalityCardHolder holder, int position) {
        String myText = texts.get(position);
        Integer myIcon = icons.get(position);

        holder.textFunctionality.setText(myText);
        holder.iconFunctionality.setImageResource(myIcon);

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public class FunctionalityCardHolder extends RecyclerView.ViewHolder {

        TextView textFunctionality;
        ImageView iconFunctionality;

        public FunctionalityCardHolder(@NonNull View itemView) {
            super(itemView);
            textFunctionality = itemView.findViewById(R.id.text_functionality);
            iconFunctionality = itemView.findViewById(R.id.image_functionality);
        }
    }



}
