package com.practicum.playlistmaker.settings.domain.interfaces

interface SettingsRepository {
    fun saveTheme(currentTheme: Boolean)
    fun isNight():Boolean
}