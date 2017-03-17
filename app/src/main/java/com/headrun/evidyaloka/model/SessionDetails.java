package com.headrun.evidyaloka.model;

import android.provider.CalendarContract;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by sujith on 6/3/17.
 */

public class SessionDetails {

    @SerializedName("session_attandance")
    public Attandance[] session_attandance;
    @SerializedName("topics")
    public Topic[] topics;
    @SerializedName("feedback_details")
    public Feedback[] feedback_details;

    @SerializedName("center")
    public String center;
    @SerializedName("day_num")
    public String day_num;
    @SerializedName("grade")
    public String grade;
    @SerializedName("year")
    public String year;
    @SerializedName("day_text")
    public String day_text;
    @SerializedName("start")
    public String start_time;
    @SerializedName("teacher")
    public String teacher;
    @SerializedName("subject")
    public String subject;
    @SerializedName("date")
    public String date;
    @SerializedName("session_status")
    public String session_status;
    @SerializedName("comments")
    public String comments;
    @SerializedName("center_image")
    public String center_image;
    @SerializedName("cancel_reasons")
    public LinkedHashMap<String, String> cancel_reasons;


    public class Attandance {

        @SerializedName("name")
        public String name;
        @SerializedName("is_present")
        public String is_present;
        @SerializedName("id")
        public String id;

        @Override
        public String toString() {
            return name;
        }
    }

    public class Topic {

        @SerializedName("url")
        public String url;
        @SerializedName("title")
        public String title;
        @SerializedName("id")
        public String id;
    }

    public class Feedback {

        @SerializedName("actual")
        public boolean actual;
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;

        @Override
        public String toString() {
            return title;
        }

    }

}
