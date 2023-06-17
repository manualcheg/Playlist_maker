package com.practicum.playlistmaker.main.domain

import com.practicum.playlistmaker.main.domain.MainInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository

class MainInteractorImpl(private val repository: MainRepository):MainInteractor {
    override fun openSearch() {
        repository.openSearch()
    }

    override fun openMediateka() {
        repository.openMediateka()
    }

    override fun openSettings() {
        repository.openSettings()
    }
}