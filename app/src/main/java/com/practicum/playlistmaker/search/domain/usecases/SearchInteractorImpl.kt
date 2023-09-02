package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.SearchResult
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun searchTracks(expression: String): Flow<SearchResult> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    SearchResult(result.data, null)
                }
                is Resource.Error -> {
                    SearchResult(null, result.message)
                }
            }
        }
    }

    override suspend fun addTrackToHistoryList(track: Track) {

        val selectedTracks = repository.getDataFromLocalStorage() as ArrayList<Track>
        selectedTracks.remove(selectedTracks.find { it.trackId == track.trackId })

        if (selectedTracks.size >= 10) {
            selectedTracks.removeLast()
        }
        selectedTracks.add(0, track)
        repository.saveSearchHistoryList(selectedTracks)
    }

    override suspend fun getHistoryList(): List<Track> {
        return repository.getDataFromLocalStorage()
    }

    override fun clearHistory() {
        repository.clearHistoryInStorage()
    }
}