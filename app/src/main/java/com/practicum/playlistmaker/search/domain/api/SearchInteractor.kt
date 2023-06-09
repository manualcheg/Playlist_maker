package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.entities.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)

    interface SearchConsumer {
        fun consume(foundTracks: List<Track>)
    }
}