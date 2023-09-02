package com.practicum.playlistmaker.mediateka.playlists.domain.usecases

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBRepository
import kotlinx.coroutines.flow.Flow

class PlaylistDBInteractorImpl(private val playlistDBRepository: PlaylistDBRepository):
    PlaylistDBInteractor {
    override suspend fun putPlaylist(playlist: Playlist) {
        playlistDBRepository.putPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return playlistDBRepository.getPlaylists()
    }

    override suspend fun getTracksIds(nameOfPlaylist: String): String {
        TODO("Not yet implemented")
    }

    /*override suspend fun putTrackInPlaylist(trackId:String, playlistId:Long) {
        TODO("Not yet implemented")


        playlistDBRepository.putPlaylist(updatedPlaylist)
    }*/
}