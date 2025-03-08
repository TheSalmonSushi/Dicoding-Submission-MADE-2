package com.salmonboy.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int,
    val name: String,
    val summary: String,
    val description: String,
    val imageLogo: String,
    val ownerName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val link: String,
    val isBookmarked: Boolean
): Parcelable