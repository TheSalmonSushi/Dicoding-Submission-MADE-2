package com.salmonboy.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class EventResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("imageLogo")
    val imageLogo: String,

    @field:SerializedName("ownerName")
    val ownerName: String,

    @field:SerializedName("quota")
    val quota: Int,

    @field:SerializedName("registrants")
    val registrants: Int,

    @field:SerializedName("beginTime")
    val beginTime: String,

    @field:SerializedName("link")
    val link: String
)
