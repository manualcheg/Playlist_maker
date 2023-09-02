package com.practicum.playlistmaker.mediateka.playlists.domain.interfaces

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistDBInteractor {
    suspend fun putPlaylist(playlist: Playlist)

    suspend fun getPlaylist(): Flow<List<Playlist>>

    suspend fun getTracksIds(nameOfPlaylist:String): String

//    suspend fun putTrackInPlaylist(trackId:String, playlistId:Long)

//    suspend fun updateTrack(track:PlaylistUpdateTuple)
}