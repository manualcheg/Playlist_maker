package com.practicum.playlistmaker.settings.domain.interfaces

interface SettingsInteractor {
    fun setTheme(currentTheme:Boolean)
    fun getTheme():Boolean
}