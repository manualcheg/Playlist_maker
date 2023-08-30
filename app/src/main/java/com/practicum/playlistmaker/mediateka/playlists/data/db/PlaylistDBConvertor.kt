package com.practicum.playlistmaker.mediateka.playlists.data.db

import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist

class PlaylistDBConvertor {

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCover = playlist.playlistCover,
            listOfTracksId = playlist.listOfTracksId,
            countOfTracks = playlist.countOfTracks
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCover = playlist.playlistCover,
            listOfTracksId = playlist.listOfTracksId,
            countOfTracks = playlist.countOfTracks
        )
    }
}