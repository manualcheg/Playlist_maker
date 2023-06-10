package com.practicum.playlistmaker.utils

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl

object Creator{

    private fun getSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient(context), context)
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(context))
    }
}