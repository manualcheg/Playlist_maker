package com.practicum.playlistmaker.mediateka.playlists.domain.entities

data class Playlist(
    var playlistId: Long = 0,
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
    var listOfTracksId: String?,
    var countOfTracks: Int,
)
