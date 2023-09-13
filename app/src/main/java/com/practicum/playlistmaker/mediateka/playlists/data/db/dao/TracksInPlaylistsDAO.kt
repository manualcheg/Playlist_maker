package com.practicum.playlistmaker.mediateka.playlists.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TracksInPlaylistsEntity)

    @Query("SELECT * FROM track_in_playlists")
    suspend fun getAllTracks():List<TracksInPlaylistsEntity>

    @Query("DELETE FROM track_in_playlists WHERE trackId LIKE :trackId")
    suspend fun delTrack(trackId:String)
}