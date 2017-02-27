package com.headrun.evidyaloka.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 2/2/17.
 */

public class BlockReleaseDemand {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public String data;

}
