package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Resource

interface SearchRepository {
    fun searchTracks(expression:String): Resource<List<Track>>
    fun getDataFromLocalStorage():ArrayList<Track>
    fun saveDataToStorage(track:Track)
    fun clearHistoryInStorage()
    fun saveSearchHistoryList(historyList: ArrayList<Track>)
}