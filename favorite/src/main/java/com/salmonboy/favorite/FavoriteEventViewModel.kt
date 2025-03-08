package com.salmonboy.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmonboy.core.domain.model.Event
import com.salmonboy.core.domain.usecase.EventUseCase
import kotlinx.coroutines.launch


class FavoriteEventViewModel(eventUseCase: EventUseCase): ViewModel() {
    private val _favoriteEvent = MutableLiveData<List<Event>>()
    val favoriteEvent: LiveData<List<Event>> get() = _favoriteEvent

    init {
        viewModelScope.launch {
            eventUseCase.getBookmarkEvents().collect { events ->
                _favoriteEvent.postValue(events)
            }
        }
    }
}