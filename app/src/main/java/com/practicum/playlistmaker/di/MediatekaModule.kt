package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.mediateka.playlists.data.db.PlaylistDBConvertor
import com.practicum.playlistmaker.mediateka.playlists.data.db.PlaylistDBRepositoryImpl
import com.practicum.playlistmaker.mediateka.playlists.data.db.PlaylistsDB
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBFavourites
import com.practicum.playlistmaker.mediateka.favourites.db.TrackDBConvertor
import com.practicum.playlistmaker.mediateka.favourites.db.TracksDBRepositoryImpl
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBInteractor
import com.practicum.playlistmaker.mediateka.playlists.domain.interfaces.PlaylistDBRepository
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBInteractor
import com.practicum.playlistmaker.mediateka.favourites.domain.interfaces.TracksDBRepository
import com.practicum.playlistmaker.mediateka.playlists.domain.usecases.PlaylistDBInteractorImpl
import com.practicum.playlistmaker.mediateka.favourites.domain.usecases.TracksDBInteractorImpl
import com.practicum.playlistmaker.mediateka.favourites.presentation.viewmodels.FavouritesFragmentViewModel
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistCreateViewModel
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistWorkFragmentViewModel
import com.practicum.playlistmaker.mediateka.playlists.presentation.viewmodels.PlaylistsFragmentViewModel
import org.koin.android.ext.koin.androidApplication
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
        ).fallbackToDestructiveMigration().build()
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
        ).fallbackToDestructiveMigration().build()
    }

    factory { PlaylistDBConvertor() }

    single<PlaylistDBRepository> { PlaylistDBRepositoryImpl(get(),get()) }

    single<PlaylistDBInteractor> { PlaylistDBInteractorImpl(get()) }

    viewModel {
        PlaylistCreateViewModel(get())
    }

    viewModel{
        PlaylistsFragmentViewModel(get())
    }

    viewModel{
        PlaylistWorkFragmentViewModel(get(),androidApplication())
    }
}