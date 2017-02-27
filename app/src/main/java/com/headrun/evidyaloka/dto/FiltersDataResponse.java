package com.headrun.evidyaloka.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 19/1/17.
 */

public class FiltersDataResponse implements Parcelable {

    public static String TAG = "FiltersDataResponse";

    @SerializedName("languages")
    public String[] languages;
    @SerializedName("states")
    public String[] states;
    @SerializedName("sessions_filters")
    public String[] sessions_filters;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.languages);
        dest.writeStringArray(this.states);
    }

    protected FiltersDataResponse(Parcel in) {
        this.languages = in.createStringArray();
        this.states = in.createStringArray();
    }

    public static final Creator<FiltersDataResponse> CREATOR = new Creator<FiltersDataResponse>() {
        @Override
        public FiltersDataResponse createFromParcel(Parcel source) {
            return new FiltersDataResponse(source);
        }

        @Override
        public FiltersDataResponse[] newArray(int size) {
            return new FiltersDataResponse[size];
        }
    };
}
