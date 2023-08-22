package com.practicum.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.mediateka.data.db.dao.FavouritesDao
import com.practicum.playlistmaker.mediateka.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class TracksDBFavourites: RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDao
}