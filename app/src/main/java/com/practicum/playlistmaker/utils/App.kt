package com.practicum.playlistmaker.utils

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.mainModule
import com.practicum.playlistmaker.di.playerModule
import com.practicum.playlistmaker.di.searchModule
import com.practicum.playlistmaker.di.settingsModule
import com.practicum.playlistmaker.utils.Constants.Companion.PLAYLISTMAKER_SHAREDPREFS
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val DARK_THEME = "Theme_is_dark"
var darkTheme: Boolean = false

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(playerModule, searchModule, settingsModule, mainModule)
        }

        val sharedPrefs = getSharedPreferences(PLAYLISTMAKER_SHAREDPREFS, MODE_PRIVATE)
        /* Восстанавливаем сохраненную тему */
        darkTheme = sharedPrefs.getBoolean(DARK_THEME, darkThemeCheck(this))
        switchTheme(darkTheme)
    }
    //     Метод, который проверяет включенность тёмной темы
    companion object {

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