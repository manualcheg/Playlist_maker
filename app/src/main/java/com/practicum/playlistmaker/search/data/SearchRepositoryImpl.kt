package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTracks(expression: String): List<Track> {
        var result: List<Track> = emptyList()
        if (expression.isNotEmpty()) {
            /*placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderButtonReload.visibility = View.GONE
            layoutOfListenedTracks.visibility = View.GONE
            progressBar.visibility = View.VISIBLE*/

            val response = networkClient.doRequest(TrackSearchRequest(expression))

            /*itunesService.search(userInputText)
                .enqueue(object : Callback<TrackSearchResponse> {
                    override fun onResponse(
                        call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>
                    ) {
                        progressBar.visibility = View.GONE*/
            when (response.resultCode) {
                200 -> {        //success
                    //преобразование TrackDTO в Track
                    result = (response as TrackSearchResponse).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    /*if (response.body()?.results?.isNotEmpty() == true) {
                       *//* placeholderMessage.visibility = View.GONE
                                    placeholderImage.visibility = View.GONE
                                    placeholderButtonReload.visibility = View.GONE*//*
                                    trackListAdapter.setTracks(
                                        response.body()?.results!!
                                    )
                                    showMessage("", "")
                                } else {
                                    showMessage(getString(R.string.nothing_found),
                                        SearchActivity.NOTHING_FOUND
                                    )
                                }*/
                }

                else -> {
                    result = emptyList()
                    /*//error with server answer
                    showMessage(
                        getString(R.string.something_went_wrong),
                        SearchActivity.SOMETHING_WENT_WRONG
                    )*/
                }
            }
        }

        /*                    fun onFailure( //error without server answer
                                call: Call<TrackSearchResponse>, t: Throwable
                            ) {
        *//*                        progressBar.visibility = View.GONE
                        showMessage(getString(R.string.something_went_wrong),
                            SearchActivity.SOMETHING_WENT_WRONG
                        )*//*
                    }*/
        /*                })*/
        /*        }*/
        return result
    }
}