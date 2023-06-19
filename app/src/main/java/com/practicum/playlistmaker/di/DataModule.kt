package com.practicum.playlistmaker.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module{
    // возможно это не нужно, а там, где нужен контекст просто передать в параметры - androidContext()
    single<Context> {
        androidContext()
    }
}