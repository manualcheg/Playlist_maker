package com.practicum.playlistmaker.search.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource


class SearchRepositoryImpl(private val networkClient: NetworkClient, private val context: Context) : SearchRepository {
    /*override fun searchTracks(expression: String): Resource<List<Track>> {
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
    }*/

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к сети Интернет")
            }

            200 -> {
                if ((response as TrackSearchResponse).results.isEmpty()){
                    Resource.Error(message = context.getString(R.string.nothing_found), data = null)
                } else {
                    Resource.Success(response.results.map {
                        Track(trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = it.trackTime,
                            artworkUrl100 = it.artworkUrl100,
                            trackId = it.trackId,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl)
                    })
                }
            }
            else -> {
                return Resource.Error("Ошибка сервера")
            }
        }
    }
}