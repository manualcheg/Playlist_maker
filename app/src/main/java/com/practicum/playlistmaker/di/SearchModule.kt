package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.data.SearchStorage
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.utils.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(context = androidContext())
    }

    factory<SearchStorage> {
        SearchStorageImpl(sharedPrefs = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            Constants.SHARED_PREFS_SELECTED_TRACKS,
            Context.MODE_PRIVATE
        )
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get(),
            searchStorage = get(),
            context = androidContext()
        )
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    viewModel {
        SearchViewModel(application = androidApplication())
    }
}