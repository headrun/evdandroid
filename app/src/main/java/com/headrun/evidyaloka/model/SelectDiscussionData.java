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

    public class Selection_Slot_Data {
        @SerializedName("current_slot")
        public LinkedHashMap<String, String> current_slot;
        @SerializedName("available_slots")
        public LinkedHashMap<String, LinkedHashMap<String, String>> available_slots;
        @SerializedName("month")
        public int month;
        @SerializedName("year")
        public int year;
    }

}
