package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.entities.Track

const val SHARED_PREFS_SELECTED_TRACKS = "Shared prefs selected tracks"

class SearchHistory(sharedPrefs: SharedPreferences) {
    private var sPrefs = sharedPrefs
    private val json = sPrefs.getString(SELECTED_TRACKS, "[]") // костыль - null по умолчанию быть не должно
    private val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
    private var selectedTracks: ArrayList<Track> = Gson().fromJson(json, typeToken)

    init{
            if (sPrefs != null){
                sPrefs = sharedPrefs
            }
    }
        fun save(track: Track) {
        selectedTracks.remove(selectedTracks.find {it.trackId == track.trackId})

        if (selectedTracks.size >= 10) {
            selectedTracks.removeLast()
        }
        selectedTracks.add(0, track)

        sPrefs.edit()
            .putString(SELECTED_TRACKS, Gson().toJson(selectedTracks))
            .apply()
    }

    fun clear() {
        sPrefs.edit()
            .clear()
            .apply()
    }

    fun read(): ArrayList<Track> {
        return selectedTracks
    }

}