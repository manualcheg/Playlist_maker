package com.practicum.playlistmaker.mediateka.playlists.data.db

import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBRepository
import com.practicum.playlistmaker.search.domain.entities.Track
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

    override suspend fun putTrackInDB(track: Track) {
        playlistsDB.tracksInPlaylistsDao().addTrack(playlistDBConvertor.map(track))
    }

    override suspend fun getPlaylist(playlistID: Long): Playlist {
        return playlistsDB.playlistsDao().getPlaylist(playlistID)
    }

    override suspend fun getTracksOfPlaylist(tracksIds: List<String>): Flow<List<Track>> = flow {
        val tracks = playlistsDB.tracksInPlaylistsDao().getAllTracks()
        val requestedTracks: ArrayList<Track> = arrayListOf()
        for (track in tracks) {
            if (tracksIds.contains(track.trackId)) {
                requestedTracks.add(playlistDBConvertor.map(track))
            }
        }
        emit(requestedTracks)
    }
}