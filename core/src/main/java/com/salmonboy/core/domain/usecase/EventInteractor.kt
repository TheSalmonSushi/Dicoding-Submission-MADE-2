package com.salmonboy.core.domain.usecase

import com.salmonboy.core.data.Resource
import com.salmonboy.core.domain.model.Event
import com.salmonboy.core.domain.repository.IEventRepository
import kotlinx.coroutines.flow.Flow

class EventInteractor(private val eventRepository: IEventRepository): EventUseCase {
    override fun getAllEvents(): Flow<Resource<List<Event>>> =
        eventRepository.getAllEvent()

    override fun getBookmarkEvents(): Flow<List<Event>> =
        eventRepository.getBookmarkEvents()

    override fun setBookmarkEvent(event: Event, state: Boolean) =
        eventRepository.setBookmarkEvents(event, state)

}