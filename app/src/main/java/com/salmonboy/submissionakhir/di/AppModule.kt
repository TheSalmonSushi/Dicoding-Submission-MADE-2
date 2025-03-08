package com.salmonboy.submissionakhir.di

import com.salmonboy.core.domain.usecase.EventInteractor
import com.salmonboy.core.domain.usecase.EventUseCase
import com.salmonboy.submissionakhir.ui.detail.EventDetailViewModel
import com.salmonboy.submissionakhir.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<EventUseCase> { EventInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { EventDetailViewModel(get()) }
}