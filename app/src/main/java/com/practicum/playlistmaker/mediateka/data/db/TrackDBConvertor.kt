package com.practicum.playlistmaker.mediateka.data.db

import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.mediateka.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.entities.Track

class TrackDBConvertor {
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            country = track.country,
//            timeOfAddition = java.time.LocalTime.now().toString(),
            timeOfAddition = System.currentTimeMillis().toString(),
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            country = track.country,
            inFavourite = true
        )
    }
}