package com.practicum.playlistmaker.utils

class Constants {
    companion object {
        //константа-ключ для поиска в Bundle сохраненного состояния:
        const val USERTEXT ="USER_INPUT"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SELECTED_TRACKS = "Selected_tracks"
        const val SHARED_PREFS_SELECTED_TRACKS = "Shared prefs selected tracks"
        val PLAYER_TIMER_TOKEN = Any()
        const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
        const val _00_00 = "00:00"
    }
}