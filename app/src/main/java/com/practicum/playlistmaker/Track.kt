package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    var trackName: String,
    var artistName: String,
    @SerializedName("trackTimeMillis") var trackTime: String,
    var artworkUrl100: String,
    var trackId: String
) {}
