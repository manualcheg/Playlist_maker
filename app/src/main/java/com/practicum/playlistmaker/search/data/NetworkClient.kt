package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.SearchResponse

interface NetworkClient {
    fun doRequest(dto: Any): SearchResponse
}