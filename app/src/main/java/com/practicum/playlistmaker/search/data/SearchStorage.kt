package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.entities.Track

interface SearchStorage {
    fun getData():ArrayList<Track>
    fun saveData(track:Track)
    fun clearHistory()
    fun saveSearchHistoryList(historyList: ArrayList<Track>)
}