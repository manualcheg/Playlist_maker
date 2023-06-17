package com.practicum.playlistmaker.utils

import android.content.Context
import androidx.activity.ComponentActivity
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.settings.domain.usecases.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository
import com.practicum.playlistmaker.sharing.domain.usecases.SharingInteractorImpl

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

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(context))
    }
}