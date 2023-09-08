package com.practicum.playlistmaker.mediateka.favourites.domain.usecases

import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBInteractor
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

class TracksDBInteractorImpl(private val tracksDBRepository: TracksDBRepository) :
    TracksDBInteractor {
    override suspend fun putTrack(track: Track) {
        tracksDBRepository.putTrack(track)
    }

    override suspend fun delTrack(track: Track) {
        tracksDBRepository.delTrack(track)
    }

    override suspend fun getFavourites(): Flow<List<Track>> {
        return tracksDBRepository.getFavourites()
    }

    override suspend fun getFavouritesTracksIds(): List<String> {
        return tracksDBRepository.getIds()
    }
}