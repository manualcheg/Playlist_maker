package com.practicum.playlistmaker.mediateka.data.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "tracks_liked")
data class TrackEntity(
    var trackName: String?,
    var artistName: String?,
    var trackTime: String?,
    var artworkUrl100: String?,
    @PrimaryKey
    var trackId: String,
    var collectionName: String?,
    var releaseDate: String?,
    var primaryGenreName: String?,
    var country: String?,
    var previewUrl: String?,
    @ColumnInfo(name = "time_of_addition")
    var timeOfAddition: String?
)