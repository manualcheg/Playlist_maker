package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.entities.Track

interface SearchRepository {
    fun searchTracks(expression:String): List<Track>
}