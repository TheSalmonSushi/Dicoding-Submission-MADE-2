package com.salmonboy.core.data.source.local.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.salmonboy.core.data.source.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM event ORDER BY begin_time ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM event WHERE bookmarked = 1")
    fun getBookmarkedEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Update
    fun updateBookmarkedEvent(event: EventEntity)
}