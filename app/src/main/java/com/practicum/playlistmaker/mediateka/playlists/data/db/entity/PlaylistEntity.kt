package com.practicum.playlistmaker.mediateka.playlists.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var playlistId: Long = 0,
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
    var listOfTracksId: String?,
    var countOfTracks: Int,
)
