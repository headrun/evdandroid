package com.headrun.evidyaloka.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sujith on 2/3/17.
 */

public class LocData {

    @SerializedName("status")
    public String status;
    @SerializedName("tp")
    public String tp;
    @SerializedName("msg")
    public String msg;
    @SerializedName("result")
    public LinkedHashMap<String, String> loc_data;
}
