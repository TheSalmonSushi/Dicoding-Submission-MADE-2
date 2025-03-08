package com.salmonboy.core.domain.repository

import com.salmonboy.core.data.Resource
import com.salmonboy.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    fun getAllEvent(): Flow<Resource<List<Event>>>
    fun getBookmarkEvents(): Flow<List<Event>>
    fun setBookmarkEvents(event: Event, state: Boolean)
}