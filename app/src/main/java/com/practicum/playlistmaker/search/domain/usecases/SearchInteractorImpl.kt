package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun addTrackToHistoryList(track: Track) {

        val selectedTracks = repository.getDataFromLocalStorage()
        selectedTracks.remove(selectedTracks.find { it.trackId == track.trackId })

        if (selectedTracks.size >= 10) {
            selectedTracks.removeLast()
        }
        selectedTracks.add(0, track)
        repository.saveSearchHistoryList(selectedTracks)
    }

    override fun getHistoryList(): ArrayList<Track> {
        return repository.getDataFromLocalStorage()
    }

    override fun clearHistory(){
        repository.clearHistoryInStorage()
    }
}