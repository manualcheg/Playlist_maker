package com.practicum.playlistmaker.settings.domain.interfaces

interface SettingsRepository {
    fun saveTheme(isNightOutside: Boolean)
    fun isNight():Boolean
}