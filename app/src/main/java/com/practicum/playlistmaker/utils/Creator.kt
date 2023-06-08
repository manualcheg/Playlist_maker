package com.practicum.playlistmaker.utils

import com.practicum.playlistmaker.search.domain.SearchInteractor

object Creator{

    fun getSearchRepository():SearchRepository{
        return SearchRepositoryImpl()
    }

    fun provideSearchInteractor(): SearchInteractor{
        return SearchInteractorImpl()
    }
}