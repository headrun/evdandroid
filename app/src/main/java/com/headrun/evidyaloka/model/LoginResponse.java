package com.headrun.evidyaloka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 1/2/17.
 */

public class LoginResponse implements Parcelable {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public userData data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
    }

    public LoginResponse() {
    }

    protected LoginResponse(Parcel in) {
        this.status = in.readInt();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<LoginResponse> CREATOR = new Parcelable.Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };


    public class userData {

        @SerializedName("status")
        public String status;
        @SerializedName("roles")
        public String[] roles;
        @SerializedName("id")
        public String id;
        @SerializedName("username")
        public String username;
        @SerializedName("profile_complete_status")
        public String profile_complete_status;
        @SerializedName("picture")
        public String picture;

        @SerializedName("pref_days")
        public String pref_days;
        @SerializedName("internet_connection")
        public String internet_connection;
        @SerializedName("date_joined")
        public String date_joined;
        @SerializedName("trainings_complete")
        public String trainings_complete;
        @SerializedName("fbatwork_id")
        public String fbatwork_id;
        @SerializedName("code_conduct")
        public String code_conduct;
        @SerializedName("last_login")
        public String last_login;
        @SerializedName("evd_rep_meet")
        public String evd_rep_meet;
        @SerializedName("fb_member_token")
        public String fb_member_token;
        @SerializedName("self_eval")
        public String self_eval;
        @SerializedName("dicussion_outcome")
        public String dicussion_outcome;
        @SerializedName("headset")
        public String headset;
        @SerializedName("evd_rep")
        public String evd_rep;
        @SerializedName("pref_slots")
        public String pref_slots;
        @SerializedName("is_active")
        public String is_active;
        @SerializedName("hrs_contributed")
        public String hrs_contributed;

        @SerializedName("trail_class")
        public String trail_class;
        @SerializedName("remarks")
        public String remarks;
        @SerializedName("webcam")
        public String webcam;
        @SerializedName("review_resources")
        public String review_resources;
        @SerializedName("pref_subjects")
        public String pref_subjects;
        @SerializedName("time_zone")
        public String time_zone;
        @SerializedName("pref_center")
        public String pref_center;
        @SerializedName("computer")
        public String computer;
        @SerializedName("unavailability_reason")
        public String unavailability_reason;
        @SerializedName("purpose")
        public String purpose;

        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("gender")
        public String gender;
        @SerializedName("age")
        public String age;

        @SerializedName("email")
        public String email;
        @SerializedName("secondary_email")
        public String secondary_email;
        @SerializedName("skype_id")
        public String skype_id;
        @SerializedName("phone")
        public String phone;

        @SerializedName("profession")
        public String profession;
        @SerializedName("pref_medium")
        public String pref_medium;
        @SerializedName("reference_channel")
        public String reference_channel;
        @SerializedName("referer")
        public String referer;

        @SerializedName("country")
        public String country;
        @SerializedName("state")
        public String state;
        @SerializedName("city")
        public String city;
        @SerializedName("short_notes")
        public String short_notes;


    }
}
