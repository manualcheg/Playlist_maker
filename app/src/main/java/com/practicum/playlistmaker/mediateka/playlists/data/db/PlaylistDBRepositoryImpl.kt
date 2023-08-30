package com.practicum.playlistmaker.mediateka.playlists.data.db

import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDBRepositoryImpl(
    private val playlistsDB: PlaylistsDB,
    private val playlistDBConvertor: PlaylistDBConvertor
) : PlaylistDBRepository {
    override suspend fun putPlaylist(playlist: Playlist) {
        playlistsDB.playlistsDao().putPlaylist(playlistDBConvertor.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistsDB.playlistsDao().getPlaylists()
        emit(mapList(playlists))
    }

    private fun mapList(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDBConvertor.map(playlist) }
    }


    /*override suspend fun getTracksId(playlistName:String): String? {
        return playlistsDB.playlistsDao().getTracksId(playlistName)
    }*/
}