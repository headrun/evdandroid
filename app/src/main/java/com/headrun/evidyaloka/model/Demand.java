package com.headrun.evidyaloka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 10/1/17.
 */

public class Demand implements Parcelable {

    public static final String TAG_DEMAND = "Demand";

    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public String id;
    @SerializedName("image")
    public String image;
    @SerializedName("pending_courses")
    public int pending_courses;
    @SerializedName("running_courses")
    public int running_courses;
    @SerializedName("title")
    public String title;
    @SerializedName("tags")
    public Tags tags;
    public SchoolDetails Scholle_deatils;

    public static class Tags implements Parcelable {
        @SerializedName("months")
        public String[] months;
        @SerializedName("subjects")
        public String[] subjects;

        protected Tags(Parcel in) {
            months = in.createStringArray();
            subjects = in.createStringArray();
        }

        public static final Creator<Tags> CREATOR = new Creator<Tags>() {
            @Override
            public Tags createFromParcel(Parcel in) {
                return new Tags(in);
            }

            @Override
            public Tags[] newArray(int size) {
                return new Tags[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(months);
            dest.writeStringArray(subjects);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeInt(this.pending_courses);
        dest.writeInt(this.running_courses);
        dest.writeString(this.title);
        dest.writeParcelable(this.tags, flags);
        dest.writeParcelable(this.Scholle_deatils, flags);
    }

    public Demand() {
    }

    protected Demand(Parcel in) {
        this.description = in.readString();
        this.id = in.readString();
        this.image = in.readString();
        this.pending_courses = in.readInt();
        this.running_courses = in.readInt();
        this.title = in.readString();
        this.tags = in.readParcelable(Tags.class.getClassLoader());
        this.Scholle_deatils = in.readParcelable(SchoolDetails.class.getClassLoader());
    }

    public static final Creator<Demand> CREATOR = new Creator<Demand>() {
        @Override
        public Demand createFromParcel(Parcel source) {
            return new Demand(source);
        }

        @Override
        public Demand[] newArray(int size) {
            return new Demand[size];
        }
    };
}
