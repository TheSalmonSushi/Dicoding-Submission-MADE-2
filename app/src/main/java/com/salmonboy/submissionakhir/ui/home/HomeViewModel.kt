package com.salmonboy.submissionakhir.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.salmonboy.core.domain.usecase.EventUseCase

class HomeViewModel(eventUseCase: EventUseCase) : ViewModel() {
    val events = eventUseCase.getAllEvents().asLiveData()
}