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
        @SerializedName("email")
        public String email;
        @SerializedName("profile_complete_status")
        public String profile_complete_status;
        @SerializedName("picture")
        public String picture;
        @SerializedName("phone")
        public boolean phone;
        @SerializedName("city")
        public boolean city;

    }
}
