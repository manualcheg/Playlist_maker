package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.mediateka.data.db.PlaylistDBRepositoryImpl
import com.practicum.playlistmaker.mediateka.data.db.PlaylistsDB
import com.practicum.playlistmaker.mediateka.data.db.TracksDBFavourites
import com.practicum.playlistmaker.mediateka.data.db.TrackDBConvertor
import com.practicum.playlistmaker.mediateka.data.db.TracksDBRepositoryImpl
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.mediateka.domain.interfaces.PlaylistDBRepository
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBInteractor
import com.practicum.playlistmaker.mediateka.domain.interfaces.TracksDBRepository
import com.practicum.playlistmaker.mediateka.domain.usecases.PlaylistDBInteractorImpl
import com.practicum.playlistmaker.mediateka.domain.usecases.TracksDBInteractorImpl
import com.practicum.playlistmaker.mediateka.presentation.viewmodels.FavouritesFragmentViewModel
import com.practicum.playlistmaker.mediateka.presentation.viewmodels.PlaylistCreateFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediatekaModule = module {
    //создание экземпляра базы данных избранного
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = TracksDBFavourites::class.java,
            name = "tracks_favourites"
        ).build()
    }

    factory { TrackDBConvertor() }

    single<TracksDBRepository> { TracksDBRepositoryImpl(get(), get()) }

    single<TracksDBInteractor> { TracksDBInteractorImpl(get()) }

    viewModel {
        FavouritesFragmentViewModel(get())
    }

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = PlaylistsDB::class.java,
            name = "playlists"
        ).build()
    }

    single<PlaylistDBRepository> { PlaylistDBRepositoryImpl(get()) }

    single<PlaylistDBInteractor> { PlaylistDBInteractorImpl(get()) }

    viewModel {
        PlaylistCreateFragmentViewModel(get())
    }
}