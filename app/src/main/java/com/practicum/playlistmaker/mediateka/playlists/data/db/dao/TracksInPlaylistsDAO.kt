package com.practicum.playlistmaker.mediateka.playlists.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TracksInPlaylistsEntity)
}