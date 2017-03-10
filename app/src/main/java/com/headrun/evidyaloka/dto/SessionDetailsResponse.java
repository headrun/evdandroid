package com.headrun.evidyaloka.dto;

import com.google.gson.annotations.SerializedName;
import com.headrun.evidyaloka.model.SessionDetails;

/**
 * Created by sujith on 7/3/17.
 */

public class SessionDetailsResponse {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public SessionDetails data;

}

