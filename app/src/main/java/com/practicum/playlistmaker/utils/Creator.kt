package com.practicum.playlistmaker.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.practicum.playlistmaker.main.data.MainRepositoryImpl
import com.practicum.playlistmaker.main.domain.MainInteractor
import com.practicum.playlistmaker.main.domain.MainInteractorImpl
import com.practicum.playlistmaker.main.domain.MainRepository
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.data.SearchStorage
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl

object Creator {

    private fun getSearchRepository(context: Context): SearchRepository {
        val sharedPrefs = context.getSharedPreferences(
            Constants.SHARED_PREFS_SELECTED_TRACKS,
            ComponentActivity.MODE_PRIVATE
        )
        return SearchRepositoryImpl(RetrofitNetworkClient(context), SearchStorageImpl(sharedPrefs), context)
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(context))
    }



/*    private fun getMainRepository(context: Context): MainRepository {
        return MainRepositoryImpl(context)
    }

    fun provideMainInteractor(context: Context): MainInteractor {
        return MainInteractorImpl(getMainRepository(context))
    }*/
}