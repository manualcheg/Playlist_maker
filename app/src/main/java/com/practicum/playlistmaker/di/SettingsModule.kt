package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.SettingsRepository
import com.practicum.playlistmaker.settings.domain.usecases.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository
import com.practicum.playlistmaker.sharing.domain.usecases.SharingInteractorImpl
import com.practicum.playlistmaker.utils.Constants.Companion.PLAYLISTMAKER_SHAREDPREFS
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SharingRepository> {
        SharingRepositoryImpl(context = androidContext())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = androidContext(), sharedPrefs = get())
    }

    factory<SharedPreferences> {
        androidContext().getSharedPreferences(PLAYLISTMAKER_SHAREDPREFS, Context.MODE_PRIVATE)
    }
    single<SharingInteractor> {
        SharingInteractorImpl(sharingRepository = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }
}