package com.practicum.playlistmaker.sharing.domain.interfaces

interface SharingRepository {
    fun sendEmail()
    fun openLink()
    fun shareApp()
}