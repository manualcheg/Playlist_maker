package com.practicum.playlistmaker.sharing.domain.usecases

import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository

class SharingInteractorImpl(val sharingRepository:SharingRepository): SharingInteractor {
    override fun sendEmail() {
        sharingRepository.sendEmail()
    }

    override fun openLink() {
        sharingRepository.openLink()
    }

    override fun shareApp() {
        sharingRepository.shareApp()
    }
}