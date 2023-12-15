package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    var trackName: String?,
    var artistName: String?,
    @SerializedName("trackTimeMillis") var trackTime: String?,
    var artworkUrl100: String?,
    var artworkUrl60: String?,
    var trackId: String?,
    var collectionName: String?,
    var releaseDate: String?,
    var primaryGenreName: String?,
    var country: String?,
    var previewUrl: String?,
)