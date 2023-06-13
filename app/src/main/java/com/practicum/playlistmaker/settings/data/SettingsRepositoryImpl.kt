package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.settings.ui.THEME_PREFS
import com.practicum.playlistmaker.utils.DARK_THEME

class SettingsRepositoryImpl(private val context: Context):SettingsRepository {
    private val sharedPrefs by lazy{
        context.getSharedPreferences(THEME_PREFS, Application.MODE_PRIVATE)
    }
    private var isNight = false

    override fun saveTheme(isNightOutside: Boolean) {
        sharedPrefs.edit().putBoolean(DARK_THEME, isNightOutside).apply()

        /*            (applicationContext as App).switchTheme(checked)
    val sharedPrefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
    sharedPrefs.edit()
        .putBoolean(DARK_THEME, darkTheme)
        .apply()*/
    }

    override fun isNight():Boolean {
        val systemThemeIsDark =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        isNight = sharedPrefs.getBoolean(DARK_THEME, systemThemeIsDark)
        return isNight
    }
}