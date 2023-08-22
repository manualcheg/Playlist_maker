package com.practicum.playlistmaker.settings.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
//        binding.settingsScreenArrowBackLikeButton.setOnClickListener { this@SettingsActivity.finish() }
    }

    private fun observeToIsNightThemeLiveData() {
        settingsViewModel.isNightLiveData.observe(viewLifecycleOwner) {
//        Проверка на включенность темной темы
            binding.switchDarkTheme.isChecked = it
        }
    }
}