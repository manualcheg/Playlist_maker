package com.practicum.playlistmaker.settings.domain.usecases

import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository

class SettingsInteractorImpl(private val repository:SettingsRepository):SettingsInteractor {
    override fun setTheme(currentTheme:Boolean) {
        repository.saveTheme(currentTheme)
    }

    override fun getTheme():Boolean {
        return repository.isNight()
    }
}