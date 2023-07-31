package com.practicum.playlistmaker.utils

class Constants {
    companion object {
        //константа-ключ для поиска в Bundle сохраненного состояния:
        const val USERTEXT = "USER_INPUT"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SELECTED_TRACKS = "Selected_tracks"
        val PLAYER_TIMER_TOKEN = Any()
        const val PLAYBACK_TIME_RENEW_DELAY_MS = 300L
        const val _00_00 = "00:00"
        const val PLAYLISTMAKER_SHAREDPREFS = "Playlistmaker shared prefs"
    }
}