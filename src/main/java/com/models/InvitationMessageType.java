package com.models;

import com.google.gson.annotations.SerializedName;

public enum InvitationMessageType {
    @SerializedName("REQUEST_RESPONSE")
    REQUEST_RESPONSE,

    @SerializedName("ERROR")
    ERROR,

    @SerializedName("INFO")
    INFO
}
