package com.practicum.playlistmaker.utils

import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl

object Creator{

    fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl()
    }

    fun provideSearchInteractor(): SearchInteractor{
        return SearchInteractorImpl()
    }
}