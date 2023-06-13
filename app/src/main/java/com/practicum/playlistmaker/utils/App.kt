package com.practicum.playlistmaker.utils

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.ui.THEME_PREFS

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
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

//     Метод, который проверяет включенность тёмной темы
    companion object {
        fun darkThemeCheck(context: Context): Boolean {
            val currentNightMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNight = when ((currentNightMode)) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            return isNight
        }
    }
}
