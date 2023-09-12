package com.practicum.playlistmaker.mediateka.favourites.db

import com.practicum.playlistmaker.mediateka.favourites.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.entities.Track

class TrackDBConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl60 =  track.artworkUrl60,
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
            artworkUrl60 =  track.artworkUrl60,
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