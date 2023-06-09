package com.practicum.playlistmaker.utils

import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl
import com.practicum.playlistmaker.search.domain.SearchRepository

object Creator{

    fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl()
    }

    fun provideSearchInteractor(): SearchInteractor{
        return SearchInteractorImpl()
    }
}