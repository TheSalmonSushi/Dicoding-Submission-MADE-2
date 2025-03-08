package com.salmonboy.core.data.source.local

import com.salmonboy.core.data.source.local.entity.EventEntity
import com.salmonboy.core.data.source.local.room.EventDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val eventDao: EventDao){

    fun getAllEvents(): Flow<List<EventEntity>> = eventDao.getAllEvents()

    fun getBookmarkedEvents(): Flow<List<EventEntity>> = eventDao.getBookmarkedEvents()

    suspend fun insertEvents(events: List<EventEntity>) = eventDao.insertEvent(events)

    fun setBookmarkEvents(event: EventEntity, newState: Boolean) {
        event.isBookmarked = newState
        eventDao.updateBookmarkedEvent(event)
    }
}