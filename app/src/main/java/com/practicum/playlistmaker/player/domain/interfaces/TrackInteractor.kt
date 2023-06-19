package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.search.domain.entities.Track

interface TrackInteractor {
    var playerState:MediaPlayerState

    fun getTrack(): Track
    fun startPlayer(): MediaPlayerState
    fun pausePlayer(): MediaPlayerState
    fun playbackControl(): MediaPlayerState
    fun preparePlayer(mediaPlayerPreparator: MediaPlayerPrepare)
    fun playerRelease()
    fun playerGetCurrentPosition():Int
    fun putPlayerState(playerState: MediaPlayerState)
    fun returnPlayerState():MediaPlayerState
}