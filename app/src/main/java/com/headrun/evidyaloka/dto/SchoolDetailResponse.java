package com.headrun.evidyaloka.dto;

import com.google.gson.annotations.SerializedName;
import com.headrun.evidyaloka.model.SchoolDetails;

/**
 * Created by sujith on 21/1/17.
 */

public class SchoolDetailResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public SchoolDetails data;
}
