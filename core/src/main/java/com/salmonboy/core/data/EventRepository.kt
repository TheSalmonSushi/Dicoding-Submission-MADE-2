package com.salmonboy.core.data

import com.salmonboy.core.data.source.local.LocalDataSource
import com.salmonboy.core.data.source.remote.RemoteDataSource
import com.salmonboy.core.data.source.remote.network.Result
import com.salmonboy.core.data.source.remote.response.EventResponse
import com.salmonboy.core.domain.model.Event
import com.salmonboy.core.domain.repository.IEventRepository
import com.salmonboy.core.utils.AppExecutors
import com.salmonboy.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors

): IEventRepository {
    override fun getAllEvent(): Flow<Resource<List<Event>>> =
        object : NetworkBoundResource<List<Event>, List<EventResponse>>() {
            override fun loadFromDB(): Flow<List<Event>> {
                return localDataSource.getAllEvents().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }
            override suspend fun createCall(): Flow<Result<List<EventResponse>>> {
                return remoteDataSource.getAllEvents()
            }
            override suspend fun saveCallResult(data: List<EventResponse>) {
                val eventList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertEvents(eventList)
            }
            override fun shouldFetch(data: List<Event>?): Boolean {
                return data.isNullOrEmpty()
            }
        }.asFlow()


    override fun getBookmarkEvents(): Flow<List<Event>> {
        return localDataSource.getBookmarkedEvents().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setBookmarkEvents(event: Event, state: Boolean) {
        val tourismEntity = DataMapper.mapDomainToEntity(event)
        appExecutors.diskIO().execute { localDataSource.setBookmarkEvents(tourismEntity, state) }
    }
}