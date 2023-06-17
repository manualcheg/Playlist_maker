package com.practicum.playlistmaker.search.domain.entities

import com.google.gson.annotations.SerializedName

data class Track(
    var trackName: String,
    var artistName: String,
    @SerializedName("trackTimeMillis") var trackTime: String,
    var artworkUrl100: String,
    var trackId: String,
    var collectionName: String,
    var releaseDate: String,
    var primaryGenreName: String,
    var country: String,
    var previewUrl: String,
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
