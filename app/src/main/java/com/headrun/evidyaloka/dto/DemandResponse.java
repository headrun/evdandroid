package com.headrun.evidyaloka.dto;

import com.google.gson.annotations.SerializedName;
import com.headrun.evidyaloka.model.Demand;

import java.util.List;

/**
 * Created by sujith on 10/1/17.
 */

public class DemandResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<Demand> data;
}
