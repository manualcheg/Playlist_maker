package com.practicum.playlistmaker.mediateka.favourites.domain.interfaces

import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface TracksDBInteractor {
    suspend fun putTrack(track: Track)

    suspend fun delTrack(track: Track)

    suspend fun getFavourites(): Flow<List<Track>>
    suspend fun getFavouritesTracksIds(): List<String>
}