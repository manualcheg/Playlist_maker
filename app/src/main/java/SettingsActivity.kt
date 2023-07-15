/*
package com.practicum.playlistmaker.settings.presentation.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val settingsViewModel: SettingsViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
        binding.settingsScreenUserAgreementTextview.setOnClickListener {
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
//        Проверка на включенность темной темы
            binding.switchDarkTheme.isChecked = it
        }
    }
}*/
