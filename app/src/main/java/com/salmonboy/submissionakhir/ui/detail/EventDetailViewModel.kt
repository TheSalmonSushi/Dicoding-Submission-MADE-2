package com.salmonboy.submissionakhir.ui.detail

import androidx.lifecycle.ViewModel
import com.salmonboy.core.domain.model.Event
import com.salmonboy.core.domain.usecase.EventUseCase


class EventDetailViewModel(private val eventUseCase: EventUseCase) : ViewModel() {
    fun setFavoriteEvent(event: Event, newStatus:Boolean) = eventUseCase.setBookmarkEvent(event, newStatus)
}