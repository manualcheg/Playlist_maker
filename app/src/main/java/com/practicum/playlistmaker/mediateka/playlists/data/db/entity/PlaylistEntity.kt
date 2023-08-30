package com.practicum.playlistmaker.mediateka.playlists.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var playlistId: Long = 0,
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
    var listOfTracksId: String?,
    var countOfTracks: Int,
) {
    private fun listOfTracksIdsToStrings(list: List<Int>): String {
        if (list.isEmpty()) return ""
        return list.joinToString(separator = ",")
    }

    private fun listOfTracksIdsToListInt(listInString: String): ArrayList<Int> {
        if (listInString.isEmpty()) return ArrayList<Int>()

        return ArrayList<Int>(listInString.split(",").map { item -> item.toInt() })
    }
}
