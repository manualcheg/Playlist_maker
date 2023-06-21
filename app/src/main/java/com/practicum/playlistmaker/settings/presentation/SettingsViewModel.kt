package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var isNightMutableLiveData = MutableLiveData<Boolean>()
    val isNightLiveData: LiveData<Boolean> = isNightMutableLiveData

    init{
        isNightMutableLiveData.value = settingsInteractor.getTheme()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendEmail() {
        sharingInteractor.sendEmail()
    }

    fun openLink() {
        sharingInteractor.openLink()
    }

    fun setTheme(switchState: Boolean) {
        settingsInteractor.setTheme(switchState)
        isNightMutableLiveData.value = switchState
    }
}