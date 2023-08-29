package com.practicum.playlistmaker.mediateka.domain.interfaces

import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistDBRepository {
    suspend fun putPlaylist(playlist: PlaylistEntity)

    suspend fun getPlaylist(): Flow<List<PlaylistEntity>>

    suspend fun getTracksId(nameOfPlaylist:String): String?
}