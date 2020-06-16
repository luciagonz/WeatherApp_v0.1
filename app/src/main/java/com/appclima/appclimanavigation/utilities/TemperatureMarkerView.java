package com.appclima.appclimanavigation.utilities;

import android.content.Context;
import android.widget.TextView;

import com.appclima.appclimanavigation.R;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class TemperatureMarkerView extends MarkerView {

    private TextView max_temperature;

    public TemperatureMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        max_temperature = findViewById(R.id.max_temperature_marker_view);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        System.out.println(e.getY());
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            max_temperature.setText("Value: " + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            max_temperature.setText("Value: " + Utils.formatNumber(e.getY(), 0, true));
        }
        super.refreshContent(e, highlight);

    }



    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
