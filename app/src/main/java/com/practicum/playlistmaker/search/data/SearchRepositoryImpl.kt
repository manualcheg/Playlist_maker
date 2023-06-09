package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTracks(expression: String): List<Track> {
        var result: List<Track> = emptyList()
        if (expression.isNotEmpty()) {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            when (response.resultCode) {
                200 -> {        //success
                                //преобразование TrackDTO в Track
                    result = (response as TrackSearchResponse).results.map {
                        Track(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = it.trackTime,
                            artworkUrl100 = it.artworkUrl100,
                            trackId = it.trackId,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )
                    }
                }

                else -> {
                    result = emptyList()
                }
            }
        }
        return result
    }
}