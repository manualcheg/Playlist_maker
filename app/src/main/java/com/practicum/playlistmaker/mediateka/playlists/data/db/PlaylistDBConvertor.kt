package com.practicum.playlistmaker.mediateka.playlists.data.db

import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.mediateka.playlists.data.db.entity.TracksInPlaylistsEntity
import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist
import com.practicum.playlistmaker.search.domain.entities.Track

class PlaylistDBConvertor {

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCover = playlist.playlistCover,
            listOfTracksId = playlist.listOfTracksId,
            countOfTracks = playlist.countOfTracks
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCover = playlist.playlistCover,
            listOfTracksId = playlist.listOfTracksId,
            countOfTracks = playlist.countOfTracks
        )
    }

    fun map(track: Track): TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl60 =  track.artworkUrl60,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            timeOfAddition = System.currentTimeMillis().toString()
        )
    }

    fun map(track: TracksInPlaylistsEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl60 =  track.artworkUrl60,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }
}