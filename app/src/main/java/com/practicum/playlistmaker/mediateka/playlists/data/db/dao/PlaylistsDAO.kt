package com.practicum.playlistmaker.mediateka.playlists.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist

@Dao
interface PlaylistsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE playlistId LIKE :playlistId")
    suspend fun getPlaylist(playlistId: Long): Playlist

    @Query("DELETE FROM playlists WHERE playlistId LIKE :playlistId")
    suspend fun delPlaylist(playlistId: Long)

/*    @Delete(entity = PlaylistEntity::class)
    suspend fun delPlaylist(playlistId: Long)*/
}