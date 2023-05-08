package com.practicum.playlistmaker

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
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

}
