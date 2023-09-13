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

    override suspend fun delTrack(trackId: String, playlistId: Long) {
        val playlist = getPlaylist(playlistId)
        val tracksIdArrayList = java.util.ArrayList(playlist.listOfTracksId?.split(",")!!)
        tracksIdArrayList.remove(trackId)
        playlist.listOfTracksId = tracksIdArrayList.joinToString(separator = ",")
        playlist.countOfTracks = tracksIdArrayList.size
        playlistsDB.playlistsDao().putPlaylist(playlistDBConvertor.map(playlist))
        removeFromTracksInPlaylistDB(trackId)
    }

    override suspend fun removeFromTracksInPlaylistDB(trackId: String) {
        var allTracksId: ArrayList<String> = arrayListOf()
        var thereIs = false
        getPlaylists().collect { playlists ->
            for (playlist in playlists) {
                playlist.listOfTracksId?.let { allTracksId.add(it) }
            }
        }

        for (trackList in allTracksId){
            if (trackList.contains(trackId)){
                thereIs = true
            }
        }

        if (!thereIs) {
            playlistsDB.tracksInPlaylistsDao().delTrack(trackId)
        }
    }
}
