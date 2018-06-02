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
    @SerializedName("course_id")
    public String id;
    @SerializedName("image")
    public String image;
    @SerializedName("pending_courses")
    public int pending_courses;
    @SerializedName("running_courses")
    public int running_courses;
    @SerializedName("grades")
    public String title;
    @SerializedName("tags")
    public Tags tags;
    @SerializedName("subject")
    public String subject;
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

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(months);
            dest.writeStringArray(subjects);
        }

        @Override
        public int describeContents() {
            return 0;
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
    }

    protected Demand(Parcel in) {
        description = in.readString();
        id = in.readString();
        image = in.readString();
        pending_courses = in.readInt();
        running_courses = in.readInt();
        title = in.readString();
        tags = in.readParcelable(Tags.class.getClassLoader());
        subject = in.readString();
        Scholle_deatils = in.readParcelable(SchoolDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeInt(pending_courses);
        dest.writeInt(running_courses);
        dest.writeString(title);
        dest.writeParcelable(tags, flags);
        dest.writeString(subject);
        dest.writeParcelable(Scholle_deatils, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Demand> CREATOR = new Creator<Demand>() {
        @Override
        public Demand createFromParcel(Parcel in) {
            return new Demand(in);
        }

        @Override
        public Demand[] newArray(int size) {
            return new Demand[size];
        }
    };
}
