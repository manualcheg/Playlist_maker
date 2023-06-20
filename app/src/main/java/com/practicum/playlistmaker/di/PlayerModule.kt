package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImpl
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(androidContext())
    }

    factory <TrackInteractor> {
        TrackInteractorImpl(get())
    }

    viewModel{
        PlayerViewModel(get())
    }
}