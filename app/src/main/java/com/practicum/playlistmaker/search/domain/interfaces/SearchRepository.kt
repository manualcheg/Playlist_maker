package com.practicum.playlistmaker.search.domain.interfaces

import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    suspend fun getDataFromLocalStorage(): List<Track>
    suspend fun saveDataToStorage(track: Track)
    fun clearHistoryInStorage()
    fun saveSearchHistoryList(historyList: ArrayList<Track>)
}