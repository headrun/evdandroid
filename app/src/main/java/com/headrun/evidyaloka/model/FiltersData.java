package com.headrun.evidyaloka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 19/1/17.
 */

public class FiltersData implements Parcelable {

    public static String TAG = "FiltersDataResponse";

    @SerializedName("languages")
    public String[] languages;
    @SerializedName("states")
    public String[] states;
    @SerializedName("sessions_filters")
    public String[] sessions_filters;

    protected FiltersData(Parcel in) {
        languages = in.createStringArray();
        states = in.createStringArray();
        sessions_filters = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(languages);
        dest.writeStringArray(states);
        dest.writeStringArray(sessions_filters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FiltersData> CREATOR = new Creator<FiltersData>() {
        @Override
        public FiltersData createFromParcel(Parcel in) {
            return new FiltersData(in);
        }

        @Override
        public FiltersData[] newArray(int size) {
            return new FiltersData[size];
        }
    };
}
