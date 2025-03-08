package com.salmonboy.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListEventResponse(

    @field:SerializedName("listEvents")
    val listEvents: List<EventResponse>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)