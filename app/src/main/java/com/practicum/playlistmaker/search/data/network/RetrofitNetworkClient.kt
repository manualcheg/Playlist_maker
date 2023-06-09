package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.SearchResponse

import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    val baseUrl = "http://itunes.apple.com/"
    var retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val itunesService = retrofit.create(ItunesApi::class.java)

    override fun doRequest(dto: Any): SearchResponse {

        if (dto is TrackSearchRequest) {

            val response = itunesService.search(dto.expression).execute()
            val body = response.body() ?: SearchResponse()
            return body.apply { resultCode = response.code() }

        } else {
            return SearchResponse().apply { resultCode = 400 }
        }
    }


}