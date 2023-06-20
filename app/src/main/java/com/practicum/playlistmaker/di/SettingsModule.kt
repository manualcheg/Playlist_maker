package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.settings.domain.usecases.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository
import com.practicum.playlistmaker.sharing.domain.usecases.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module{
    single<SharingRepository>{
        SharingRepositoryImpl(androidContext())
    }

    single<SettingsRepository>{
        SettingsRepositoryImpl(androidContext())
    }

    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    viewModel{
        SettingsViewModel(get(),get())
    }
}