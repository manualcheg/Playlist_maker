package com.practicum.playlistmaker.mediateka.playlists.domain.interfaces

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDBInteractor {
    suspend fun putPlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToDB(track: Track)

    suspend fun getPlaylist(playlistId: Long): Playlist

    suspend fun getTracksFromPlaylist(tracksId: List<String>): Flow<List<Track>>
}