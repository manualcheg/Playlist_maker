package com.practicum.playlistmaker.settings.presentation.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.utils.App
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.utils.Creator

const val THEME_PREFS = "Theme prefs"

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private lateinit var settingsViewModel: SettingsViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val settingsInteractorImpl = Creator.provideSettingsInteractor(this)
        val sharingInteractorImpl = Creator.provideSharingInteractor(this)

        createSettingsViewModel(sharingInteractorImpl, settingsInteractorImpl)

        observeToIsNightThemeLiveData()

        setListenerToArrowSettingsBack()

        setListenerToSwitchDarkTheme()

        setListenerToUserAgreementTextview()

        setListenerToShareappTextview()

        setListenerToSendMailSupportTextview()
    }

    private fun setListenerToSendMailSupportTextview() {
        //        Кнопка "Написать в поддержку"
        binding.settingsScreenSendMailSupportTextview.setOnClickListener {
            settingsViewModel.sendEmail()
        }
    }

    private fun setListenerToShareappTextview() {
        //        Кнопка "Поделится приложением"
        binding.settingsScreenShareappTextview.setOnClickListener {
            settingsViewModel.shareApp()
        }
    }

    private fun setListenerToUserAgreementTextview() {
        //      Кнопка "Пользовательское соглашение" с переходом на страницу
        binding.settingsScreenUserAgreementTextview.setOnClickListener{
            settingsViewModel.openLink()
        }
    }

    private fun setListenerToSwitchDarkTheme() {
        //        работа switch теперь тут
        binding.switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setTheme(checked)
        }
    }

    private fun setListenerToArrowSettingsBack() {
        //        стрелка выхода из экрана. На ней висит слушатель нажатия
        binding.settingsScreenArrowBackLikeButton.setOnClickListener { this@SettingsActivity.finish() }
    }

    private fun observeToIsNightThemeLiveData() {
        settingsViewModel.isNightLiveData.observe(this) {
//        Проверка на включенность темной темы и переключение switch
            binding.switchDarkTheme.isChecked = it
            (applicationContext as App).switchTheme(it)
        }
    }

    private fun createSettingsViewModel(
        sharingInteractorImpl: SharingInteractor,
        settingsInteractorImpl: SettingsInteractor
    ) {
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(sharingInteractorImpl, settingsInteractorImpl)
        )[SettingsViewModel::class.java]
    }
}