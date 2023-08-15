package com.practicum.playlistmaker.mediateka.domain.interfaces

import com.practicum.playlistmaker.mediateka.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface TracksDBInteractor {
    suspend fun putTrack(track: TrackEntity)

    suspend fun delTrack(track: TrackEntity)

    suspend fun getFavourites(): Flow<List<Track>>
}