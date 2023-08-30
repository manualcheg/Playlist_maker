package com.practicum.playlistmaker.mediateka.playlists.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.mediateka.playlists.data.db.dao.PlaylistsDAO
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistsDB : RoomDatabase() {
    abstract fun playlistsDao(): PlaylistsDAO
}