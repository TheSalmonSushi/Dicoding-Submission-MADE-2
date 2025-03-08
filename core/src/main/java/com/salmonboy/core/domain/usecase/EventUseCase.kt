package com.salmonboy.core.domain.usecase

import com.salmonboy.core.data.Resource
import com.salmonboy.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventUseCase {
    fun getAllEvents(): Flow<Resource<List<Event>>>
    fun getBookmarkEvents(): Flow<List<Event>>
    fun setBookmarkEvent(event: Event, state: Boolean)

}