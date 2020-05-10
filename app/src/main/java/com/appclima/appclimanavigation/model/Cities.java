package com.appclima.appclimanavigation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cities implements Parcelable {


    private String name;
    private String country;
    private double currentDegrees;
    private double maxDegrees;
    private double minDegrees;
    private Integer symbolWeatherID;
    private Integer locationType;


    // Constructor
    public Cities(String name, String country, double currentDegrees, double maxDegrees, double minDegrees, Integer symbolWeatherID, Integer locationType) {
        System.out.println("City added");
        this.name = name;
        this.country = country;
        this.currentDegrees = currentDegrees;
        this.maxDegrees = maxDegrees;
        this.minDegrees = minDegrees;
        this.symbolWeatherID = symbolWeatherID;
        this.locationType = locationType;
    }


    // Parceable constructor:
    protected Cities(Parcel in) {
        name = in.readString();
        country = in.readString();
        currentDegrees = in.readDouble();
        maxDegrees = in.readDouble();
        minDegrees = in.readDouble();
        if (in.readByte() == 0) {
            symbolWeatherID = null;
        } else {
            symbolWeatherID = in.readInt();
        }
        if (in.readByte() == 0) {
            locationType = null;
        } else {
            locationType = in.readInt();
        }
    }


    public static final Creator<Cities> CREATOR = new Creator<Cities>() {
        @Override
        public Cities createFromParcel(Parcel in) {
            return new Cities(in);
        }

        @Override
        public Cities[] newArray(int size) {
            return new Cities[size];
        }
    };

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(country);
        dest.writeDouble(currentDegrees);
        dest.writeDouble(maxDegrees);
        dest.writeDouble(minDegrees);
        if (symbolWeatherID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(symbolWeatherID);
        }
        if (locationType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(locationType);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getCurrentDegrees() {
        return currentDegrees;
    }

    public void setCurrentDegrees(double currentDegrees) {
        this.currentDegrees = currentDegrees;
    }

    public double getMaxDegrees() {
        return maxDegrees;
    }

    public void setMaxDegrees(double maxDegrees) {
        this.maxDegrees = maxDegrees;
    }

    public double getMinDegrees() {
        return minDegrees;
    }

    public void setMinDegrees(double minDegrees) {
        this.minDegrees = minDegrees;
    }

    public Integer getSymbolWeatherID() {
        return symbolWeatherID;
    }

    public void setSymbolWeatherID(Integer symbolWeatherID) {
        this.symbolWeatherID = symbolWeatherID;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }


}

