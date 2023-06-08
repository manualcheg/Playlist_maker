package com.practicum.playlistmaker.playerActivity.domain.interfaces

import com.practicum.playlistmaker.playerActivity.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.playerActivity.domain.entities.Track

interface TrackInteractor {
    fun getTrack(): Track
    fun startPlayer(): MediaPlayerState
    fun pausePlayer(): MediaPlayerState
    fun playbackControl(): MediaPlayerState
}