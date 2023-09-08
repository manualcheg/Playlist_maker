package com.practicum.playlistmaker.mediateka.playlists.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.mediateka.playlists.data.db.dao.PlaylistsDAO
import com.practicum.playlistmaker.mediateka.playlists.data.db.dao.TracksInPlaylistsDAO
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.TracksInPlaylistsEntity

@Database(version = 2, entities = [PlaylistEntity::class, TracksInPlaylistsEntity::class])
abstract class PlaylistsDB : RoomDatabase() {
    abstract fun playlistsDao(): PlaylistsDAO

    abstract fun tracksInPlaylistsDao(): TracksInPlaylistsDAO
}