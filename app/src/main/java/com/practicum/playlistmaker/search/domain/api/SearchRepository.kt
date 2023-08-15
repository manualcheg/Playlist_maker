package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    suspend fun getDataFromLocalStorage(): ArrayList<Track>
    suspend fun saveDataToStorage(track: Track)
    fun clearHistoryInStorage()
    fun saveSearchHistoryList(historyList: ArrayList<Track>)
}