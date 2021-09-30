package com.models;

import com.google.gson.annotations.SerializedName;

public enum MessageType {
    @SerializedName("ChatText")
    ChatText,

    @SerializedName("ChatImage")
    ChatImage,

    @SerializedName("ChatVideo")
    ChatVideo,

    @SerializedName("ChatAudio")
    ChatAudio,

    @SerializedName("ChatDocument")
    ChatDocument,

    @SerializedName("ChatLocation")
    ChatLocation,

    @SerializedName("ChatContact")
    ChatContact,

    @SerializedName("EventInvitationRequest")
    EventInvitationRequest,

    @SerializedName("EventInvitationResponseAccept")
    EventInvitationResponseAccept,

    @SerializedName("EventInvitationResponseReject")
    EventInvitationResponseReject,

    @SerializedName("Presence")
    Presence,

    @SerializedName("ChatMarker")
    ChatMarker,

    @SerializedName("Typing")
    Typing,

    @SerializedName("CreateGroup")
    CreateGroup,

    @SerializedName("RemoveGroup")
    RemoveGroup,

    @SerializedName("AddUsersToGroup")
    AddUsersToGroup,

    @SerializedName("RemoveGroupMembers")
    RemoveGroupMembers

}
