package com.headrun.evidyaloka.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by sujith on 28/3/17.
 */

public class SelectDiscussionData {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public Selection_Slot_Data data;


    /*   @SerializedName("result")
       public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<Selection_Slot_Data>[]>>> slots_data;
   */

    public static class Selection_Slot_Data {
        @SerializedName("booked_slots")
        public LinkedHashMap<String, Booked_slots> booked_slots;
        @SerializedName("available_slots")
        public LinkedHashMap<String, LinkedHashMap<String, String>> available_slots;
        @SerializedName("month")
        public int month;
        @SerializedName("year")
        public int year;
    }

    public static class Booked_slots {

        @SerializedName("booked_time")
        public String booked_time;
        @SerializedName("role")
        public String role;
        @SerializedName("role_id")
        public String role_id;
    }

}
