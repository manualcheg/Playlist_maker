package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SHARED_PREFS_SELECTED_TRACKS = "Shared prefs selected tracks"

class SearchHistory(sharedPrefs: SharedPreferences) {
    var sPrefs = sharedPrefs
    private val json = sPrefs.getString(SELECTED_TRACKS, "[]") // костыль - null по умолчанию быть не должно
    private val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
    private var selectedTracks: ArrayList<Track> = Gson().fromJson(json, typeToken)

    fun save(track: Track) {
        var theSame = Track("","","","","")
        if (selectedTracks.size == 10) {
            selectedTracks.removeAt(9)
        }
        for ((index,i) in selectedTracks.withIndex()) {
            if (track.trackId == i.trackId) {
//                selectedTracks.removeAt(index)
                theSame = selectedTracks[index]
            }
        }
        selectedTracks.remove(theSame)
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