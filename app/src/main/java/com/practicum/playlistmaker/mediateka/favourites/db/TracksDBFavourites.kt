package com.practicum.playlistmaker.mediateka.favourites.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.mediateka.favourites.db.dao.FavouritesDao
import com.practicum.playlistmaker.mediateka.favourites.db.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class])
abstract class TracksDBFavourites: RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDao
}