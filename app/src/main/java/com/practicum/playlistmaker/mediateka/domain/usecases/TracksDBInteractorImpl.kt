package com.practicum.playlistmaker.mediateka.domain.usecases

import com.practicum.playlistmaker.mediateka.data.db.entity.TrackEntity
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBInteractor
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

class TracksDBInteractorImpl(private val tracksDBRepository: TracksDBRepository) :
    TracksDBInteractor {
    override suspend fun putTrack(track: TrackEntity) {
        tracksDBRepository.putTrack(track)
    }

    override suspend fun delTrack(track: TrackEntity) {
        tracksDBRepository.delTrack(track)
    }

    override suspend fun getFavourites(): Flow<List<Track>> {
        return tracksDBRepository.getFavourites()
    }

    override suspend fun getFavouritesTracksIds(): List<String> {
        return tracksDBRepository.getIds()
    }
}