package com.practicum.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.App
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.utils.App.Companion.darkThemeCheck
import com.practicum.playlistmaker.utils.Creator

const val THEME_PREFS = "Theme prefs"

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

    val switchDarkTheme = findViewById<SwitchCompat>(R.id.switch_dark_theme)
    val settingsInteractorImpl = Creator.provideSettingsInteractor(this)
    val sharingInteractorImpl = Creator.provideSharingInteractor(this)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(sharingInteractorImpl, settingsInteractorImpl)
        )[SettingsViewModel::class.java]

//        settingsViewModel.getTheme()

        settingsViewModel.isNightLiveData.observe(this){
//        Проверка на включенность темной темы и переключение switch
//        darkThemeCheck(switchDarkTheme)
//        switchDarkTheme.isChecked = darkThemeCheck(this)
            switchDarkTheme.isChecked = it
            (applicationContext as App).switchTheme(it)
        }

//        switchDarkTheme.isChecked = darkThemeCheck(this)

        val arrowSettingsBack =
            findViewById<ImageView>(R.id.settings_screen_arrow_back_like_button).apply {
                setOnClickListener {this@SettingsActivity.finish()}
            }

//        работа switch теперь тут
        switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setTheme(checked)
        }

//      Кнопка "Пользовательское соглашение" с переходом на страницу
        val userAgreementTextView =
            findViewById<TextView>(R.id.settings_screen_user_agreement_textview)
        userAgreementTextView.setOnClickListener {
            settingsViewModel.openLink()
        }

//        Кнопка "Поделится приложением"
        val shareApp = findViewById<TextView>(R.id.settings_screen_shareapp_textview)
        shareApp.setOnClickListener {
            settingsViewModel.shareApp()
        }

//        Кнопка "Написать в поддержку"
        val emailToSupport = findViewById<TextView>(R.id.settings_screen_send_mail_support_textview)
        emailToSupport.setOnClickListener {
            settingsViewModel.sendEmail()
        }
    }
}