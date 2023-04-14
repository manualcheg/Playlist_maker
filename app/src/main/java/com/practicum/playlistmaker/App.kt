package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.SettingsActivity.Companion.darkThemeCheck


const val DARK_THEME = "Theme_is_dark"
var darkTheme: Boolean = false

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        /* Восстанавливаем сохраненную тему */
        darkTheme = sharedPrefs.getBoolean(DARK_THEME,darkThemeCheck(this))
        switchTheme(darkTheme)
    }

    /* работа switch */
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
