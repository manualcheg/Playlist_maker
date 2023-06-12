package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.data.SearchStorage
import com.practicum.playlistmaker.search.domain.entities.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)
    fun addTrackToHistoryList(searchStorage: SearchStorage, track: Track)
    fun getHistoryList(searchStorage: SearchStorage): ArrayList<Track>


    //Для передачи результатов поискового запроса, который будет выполняться в отдельном потоке, нужен Callback.
    // Его роль здесь выполняет интерфейс SearchConsumer
    interface SearchConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}