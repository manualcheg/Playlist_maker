package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.sharing.domain.interfaces.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.usecases.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module{

    factory<SharingInteractor>{
        SharingInteractorImpl(get())
    }
}