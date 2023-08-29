package com.practicum.playlistmaker.mediateka.data.db

import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDBRepositoryImpl(private val playlistsDB: PlaylistsDB) : PlaylistDBRepository {
    override suspend fun putPlaylist(playlist: PlaylistEntity) {
        playlistsDB.playlistsDao().putPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<PlaylistEntity>> = flow {
        val playlists = playlistsDB.playlistsDao().getPlaylists()
        emit(playlists)
    }

    /*override suspend fun getTracksId(playlistName:String): String? {
        return playlistsDB.playlistsDao().getTracksId(playlistName)
    }*/
}