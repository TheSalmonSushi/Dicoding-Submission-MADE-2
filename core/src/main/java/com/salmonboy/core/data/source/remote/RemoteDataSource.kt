package com.salmonboy.core.data.source.remote

import android.util.Log
import com.salmonboy.core.data.source.remote.network.ApiService
import com.salmonboy.core.data.source.remote.network.Result
import com.salmonboy.core.data.source.remote.response.EventResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllEvents(): Flow<Result<List<EventResponse>>> {
        return flow {
            try {
                val response = apiService.getDicodingEvents(-1, null, 100)
                val dataArray = response.listEvents
                if (dataArray.isNotEmpty()) {
                    emit(Result.Success(response.listEvents))
                } else {
                    emit(Result.Loading)
                }
            } catch (e: Exception) {
                emit(Result.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }


}