package com.salmonboy.submissionakhir

import android.app.Application
import com.salmonboy.core.di.databaseModule
import com.salmonboy.core.di.networkModule
import com.salmonboy.core.di.repositoryModule
import com.salmonboy.submissionakhir.di.useCaseModule
import com.salmonboy.submissionakhir.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}