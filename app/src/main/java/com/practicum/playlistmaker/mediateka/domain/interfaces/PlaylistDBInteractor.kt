package com.practicum.playlistmaker.mediateka.domain.interfaces

import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistDBInteractor {
    suspend fun putPlaylist(playlist:PlaylistEntity)

    suspend fun getPlaylist(): Flow<List<String>>

    suspend fun getTracksId(nameOfPlaylist:String): String

    suspend fun putTrackInPlaylist(trackId:String, nameOfPlaylist:String)
}