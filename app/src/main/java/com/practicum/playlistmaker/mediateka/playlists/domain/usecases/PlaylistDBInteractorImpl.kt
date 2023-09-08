package com.practicum.playlistmaker.mediateka.playlists.domain.usecases

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

class PlaylistDBInteractorImpl(private val playlistDBRepository: PlaylistDBRepository):
    PlaylistDBInteractor {
    override suspend fun putPlaylist(playlist: Playlist) {
        playlistDBRepository.putPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return playlistDBRepository.getPlaylists()
    }

    override suspend fun addTrackToDB(track: Track) {
        playlistDBRepository.putTrackInDB(track)
    }
}