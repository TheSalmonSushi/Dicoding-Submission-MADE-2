package com.salmonboy.core.di

import androidx.room.Room
import com.salmonboy.core.BuildConfig
import com.salmonboy.core.data.EventRepository
import com.salmonboy.core.data.source.local.LocalDataSource
import com.salmonboy.core.data.source.local.room.EventDatabase
import com.salmonboy.core.data.source.remote.RemoteDataSource
import com.salmonboy.core.data.source.remote.network.ApiService
import com.salmonboy.core.domain.repository.IEventRepository
import com.salmonboy.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module { 
    factory { get<EventDatabase>().eventDao()}
    single { 
        Room.databaseBuilder(
            androidContext(),
            EventDatabase::class.java, "Event.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module { 
    val baseUrl = BuildConfig.BASE_URL
    val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
    single { 
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single { 
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}


val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IEventRepository> {
        EventRepository(
            get(),
            get(),
            get()
        )
    }
}
