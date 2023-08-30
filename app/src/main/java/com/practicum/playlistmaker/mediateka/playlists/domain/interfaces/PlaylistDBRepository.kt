package com.practicum.playlistmaker.mediateka.playlists.domain.interfaces

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistDBRepository {
    suspend fun putPlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

//    suspend fun getTracksId(playlistName:String): String?
}