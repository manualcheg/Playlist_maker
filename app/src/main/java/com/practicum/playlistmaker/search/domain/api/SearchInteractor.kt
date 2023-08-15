package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
    suspend fun addTrackToHistoryList(track: Track)
    suspend fun getHistoryList(): ArrayList<Track>
    fun clearHistory()
}