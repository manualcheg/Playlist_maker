package com.practicum.playlistmaker.playerActivity.domain.interfaces

import com.practicum.playlistmaker.playerActivity.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.playerActivity.domain.entities.Track

interface TrackRepository {
    fun getTrack(): Track

    fun preparePlayer(mediaPlayerPreparator:MediaPlayerPrepare)

    fun startPlayer():MediaPlayerState

    fun pausePlayer():MediaPlayerState
}