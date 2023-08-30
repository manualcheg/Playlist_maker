package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.TrackRepositoryImpl
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository
import com.practicum.playlistmaker.player.domain.usecases.TrackInteractorImpl
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    factory<TrackRepository> {
        TrackRepositoryImpl(sharedPrefs = get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(trackRepository = get())
    }

    viewModel {
        PlayerViewModel(
            trackInteractorImpl = get(),
            tracksDBInteractorImpl = get(),
        )
    }
}