package com.practicum.playlistmaker.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.mediateka.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY playlistId")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT listOfTracksId FROM playlists")
    suspend fun getTracksId(nameOfPlaylist:String): String?
}

/*
* Нужно создавать плейлист - одну запись в таблице: Insert()
* Нужно добавлять трек в плейлист - получить список уже записаных, добавить к нему
* и записать обратно в поле этой записи: Query + Insert
*
*
* */