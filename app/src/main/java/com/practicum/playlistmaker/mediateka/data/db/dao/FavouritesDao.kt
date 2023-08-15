package com.practicum.playlistmaker.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.mediateka.data.db.entity.TrackEntity

@Dao
interface FavouritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putTrack(track: TrackEntity)

    @Delete
    suspend fun delTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_liked ORDER BY time_of_addition DESC")
    suspend fun getFavourites(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_liked")
    suspend fun getTracksId(): List<String>
//
//    //Для проверки присустствия в избранном
//    @Query("SELECT trackId FROM tracks_liked WHERE trackId LIKE :searchedTrackId")
//    suspend fun searchTrack(searchedTrackId: String): TrackEntity
}