package com.practicum.playlistmaker.mediateka.favourites.db

import com.practicum.playlistmaker.mediateka.favourites.db.entity.TrackEntity
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksDBRepositoryImpl(
    private val tracksDB: TracksDBFavourites,
    private val trackDbConvertor: TrackDBConvertor
) : TracksDBRepository {
    override suspend fun putTrack(track: Track) {
        tracksDB.favouritesDao().putTrack(trackDbConvertor.map(track))
    }

    override suspend fun delTrack(track: Track) {
        tracksDB.favouritesDao().delTrack(trackDbConvertor.map(track))
    }

    override suspend fun getFavourites(): Flow<List<Track>> = flow {
        val favourites = tracksDB.favouritesDao().getFavourites()
        emit(mapList(favourites))
    }

    override suspend fun getIds(): List<String> {
        return tracksDB.favouritesDao().getTracksId()
    }

    private fun mapList(favouritesList: List<TrackEntity>): List<Track> {
        return favouritesList.map { track -> trackDbConvertor.map(track)}
    }
}