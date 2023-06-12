package com.practicum.playlistmaker.utils

class Constants {
    companion object {
        //константа-ключ для поиска в Bundle сохраненного состояния:
        const val USERTEXT ="USER_INPUT"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SELECTED_TRACKS = "Selected_tracks"
        const val SHARED_PREFS_SELECTED_TRACKS = "Shared prefs selected tracks"
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}