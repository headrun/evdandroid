package com.headrun.evidyaloka.dto;

import com.google.gson.annotations.SerializedName;
import com.headrun.evidyaloka.model.Sessions;

import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 15/2/17.
 */

public class SessionResponse {
    @SerializedName("next_page")
    public int next_page;
    @SerializedName("total")
    public int total;
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public Map<String, List<Sessions>> results;
}
