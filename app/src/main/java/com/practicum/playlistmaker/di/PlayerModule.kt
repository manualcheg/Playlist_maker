package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.repository.TrackRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerModule = module{
    single {
        TrackRepositoryImpl(androidContext())
    }
    factory{
//        TrackInteractorImlp(get(),playerState)
    }
}