package com.salmonboy.core.data.source.remote.network

import com.salmonboy.core.data.source.remote.response.ListEventResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("events")
    suspend fun getDicodingEvents(
        @Query("active") active: Int = 1,
        @Query("q") query: String? = null,
        @Query("limit") page: Int = 100
    ): ListEventResponse
}