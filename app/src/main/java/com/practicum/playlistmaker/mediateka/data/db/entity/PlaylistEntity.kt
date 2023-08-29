package com.practicum.playlistmaker.mediateka.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey (autoGenerate = true)
    var playlistId: String,
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
//    @ColumnInfo(name = "listOfTracksId") @Embedded
//    var listOfTracksId: List<String>?,
    var listOfTracksId: String?,
    var countOfTracks: Int,
)