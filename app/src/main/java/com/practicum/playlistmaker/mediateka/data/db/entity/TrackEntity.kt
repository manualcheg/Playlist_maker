package com.practicum.playlistmaker.mediateka.data.db.entity

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tracks_liked")
data class TrackEntity(
    var trackName: String?,
    var artistName: String?,
    @SerializedName("trackTimeMillis") var trackTime: String?,
    var artworkUrl100: String?,
    var trackId: String?,
    var collectionName: String?,
    var releaseDate: String?,
    var primaryGenreName: String?,
    var country: String?,
    var previewUrl: String?,
    val inFavourite: Boolean,
)
