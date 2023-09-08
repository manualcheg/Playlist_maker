package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.data.SearchStorage
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.storage.SearchStorageImpl
import com.practicum.playlistmaker.search.domain.interfaces.SearchInteractor
import com.practicum.playlistmaker.search.domain.interfaces.SearchRepository
import com.practicum.playlistmaker.search.domain.usecases.SearchInteractorImpl
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(context = androidContext())
    }

    factory<SearchStorage> {
        SearchStorageImpl(sharedPrefs = get(), tracksDBFavourites = get())
    }

    //зависимость от класса SharedPreferences уже описана в SettingsModule

    factory<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get(),
            searchStorage = get(),
            context = androidContext(),
            tracksDBFavourites = get()
        )
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    viewModel {
        SearchViewModel(application = androidApplication(), searchInteractor = get())
    }
}