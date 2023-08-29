package com.practicum.playlistmaker.mediateka.domain.usecases

import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBRepository
import kotlinx.coroutines.flow.Flow

class PlaylistDBInteractorImpl(private val playlistDBRepository: PlaylistDBRepository):PlaylistDBInteractor {
    override suspend fun putPlaylist(playlist: PlaylistEntity) {
        playlistDBRepository.putPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<PlaylistEntity>> {
        return playlistDBRepository.getPlaylist()
    }

    override suspend fun getTracksIds(nameOfPlaylist: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun putTrackInPlaylist(trackId: String, nameOfPlaylist: String) {
        TODO("Not yet implemented")
    }
}