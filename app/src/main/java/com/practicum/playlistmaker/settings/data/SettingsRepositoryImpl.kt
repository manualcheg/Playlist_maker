package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.settings.presentation.ui.THEME_PREFS
import com.practicum.playlistmaker.utils.DARK_THEME

class SettingsRepositoryImpl(private val context: Context):SettingsRepository {
    private val sharedPrefs by lazy{
        context.getSharedPreferences(THEME_PREFS, Application.MODE_PRIVATE)
    }
    private var isNight = false

    override fun saveTheme(isNightOutside: Boolean) {
        sharedPrefs.edit().putBoolean(DARK_THEME, isNightOutside).apply()
    }

    override fun isNight():Boolean {
        val systemThemeIsDark =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        isNight = sharedPrefs.getBoolean(DARK_THEME, systemThemeIsDark)
        return isNight
    }
}