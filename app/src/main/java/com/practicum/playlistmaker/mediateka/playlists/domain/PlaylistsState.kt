package com.practicum.playlistmaker.mediateka.playlists.domain

import com.practicum.playlistmaker.mediateka.playlists.domain.entities.Playlist

interface PlaylistsState {

    data class Content(val playlists:List<Playlist>) : PlaylistsState

    class Empty: PlaylistsState
}
