package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}