package com.models;

import com.google.gson.annotations.SerializedName;

public enum MessageOriginality {
    @SerializedName("Original")
    Original,
    @SerializedName("Reply")
    Reply,
    @SerializedName("Forward")
    Forward
}
