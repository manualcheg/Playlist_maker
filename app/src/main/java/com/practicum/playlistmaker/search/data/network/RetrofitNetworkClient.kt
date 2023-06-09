package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient/*: NetworkClient {
    override fun doRequest(dto: Any): Response {
        val baseUrl = "http://itunes.apple.com/"
        var userInputText: String = ""
        var retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val itunesService = retrofit.create(ItunesApi::class.java)

    }
}*/