package com.salmonboy.core.utils

import com.salmonboy.core.data.source.local.entity.EventEntity
import com.salmonboy.core.data.source.remote.response.EventResponse
import com.salmonboy.core.domain.model.Event

object DataMapper {
    fun mapResponsesToEntities(input: List<EventResponse>): List<EventEntity> {
        val eventList = ArrayList<EventEntity>()
        input.map {
            val event = EventEntity(
                id = it.id,
                name = it.name,
                summary = it.summary,
                description = it.description,
                imageLogo = it.imageLogo,
                ownerName = it.ownerName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                link = it.link,
                isBookmarked = false
            )
            eventList.add(event)
        }
        return eventList
    }

    fun mapEntitiesToDomain(input: List<EventEntity>): List<Event> =
        input.map {
            Event(
                id = it.id,
                name = it.name,
                summary = it.summary,
                description = it.description,
                imageLogo = it.imageLogo,
                ownerName = it.ownerName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                link = it.link,
                isBookmarked = it.isBookmarked
            )
        }

    fun mapDomainToEntity(input: Event) = EventEntity(
        id = input.id,
        name = input.name,
        summary = input.summary,
        description = input.description,
        imageLogo = input.imageLogo,
        ownerName = input.ownerName,
        quota = input.quota,
        registrants = input.registrants,
        beginTime = input.beginTime,
        link = input.link,
        isBookmarked = input.isBookmarked
    )
}