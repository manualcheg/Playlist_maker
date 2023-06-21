package com.practicum.playlistmaker.settings.domain.usecases

import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.utils.App

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun setTheme(currentTheme: Boolean) {
        App.switchTheme(currentTheme) //переключение темы
        repository.saveTheme(currentTheme)
    }

    override fun getTheme(): Boolean {
        return repository.isNight()
    }
}