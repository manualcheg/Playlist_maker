package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.SharingRepository
import org.koin.dsl.module

val repositoryModule = module{
    single <SharingRepository>{
        SharingRepositoryImpl(get())
    }
}