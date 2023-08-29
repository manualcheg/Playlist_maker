package com.practicum.playlistmaker.mediateka.data.db.entity

data class Playlist(
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
    var listOfTracksId: String?,
    var countOfTracks: Int,
)
