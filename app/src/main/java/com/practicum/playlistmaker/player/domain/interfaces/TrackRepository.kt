package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.search.domain.entities.Track

interface TrackRepository {
    fun getTrack(): Track

    fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare)

    fun startPlayer():MediaPlayerState

    fun pausePlayer():MediaPlayerState
}