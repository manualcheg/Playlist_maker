package com.practicum.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBFavourites
import com.practicum.playlistmaker.search.data.SearchStorage
import com.practicum.playlistmaker.search.domain.entities.Track
import com.practicum.playlistmaker.utils.Constants.Companion.SELECTED_TRACKS

class SearchStorageImpl(
    private val sharedPrefs: SharedPreferences,
    private val tracksDBFavourites: TracksDBFavourites
) : SearchStorage {

    override suspend fun getData(): ArrayList<Track> {
        // костыль "[]" - null по умолчанию быть не должно
        val json = sharedPrefs.getString(SELECTED_TRACKS, "[]")
        val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
        val selectedTracks: ArrayList<Track> = Gson().fromJson(json, typeToken)
        val tracksInFavourites = tracksDBFavourites.favouritesDao().getTracksId()
        for (each in selectedTracks){
            if (tracksInFavourites.contains(each.trackId)){
                each.inFavourite=true
            }
        }
        return selectedTracks
    }

    override suspend fun saveData(track: Track) {
        val selectedTracks = getData()

        selectedTracks.remove(selectedTracks.find { it.trackId == track.trackId })

        if (selectedTracks.size >= 10) {
            selectedTracks.removeLast()
        }
        selectedTracks.add(0, track)
        saveSearchHistoryList(selectedTracks)
    }

    override fun clearHistory() {
//        sharedPrefs.edit() { clear() } - можно и так
        sharedPrefs.edit().clear().apply()
    }

    override fun saveSearchHistoryList(historyList: ArrayList<Track>) {
        val jsonString = Gson().toJson(historyList)
        sharedPrefs.edit().putString(SELECTED_TRACKS, jsonString).apply()
    }
}