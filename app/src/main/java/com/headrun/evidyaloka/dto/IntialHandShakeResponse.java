package com.headrun.evidyaloka.dto;

import com.google.gson.annotations.SerializedName;
import com.headrun.evidyaloka.model.IntialHandShake;

/**
 * Created by sujith on 13/2/17.
 */

public class IntialHandShakeResponse {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public IntialHandShake keys;
}
