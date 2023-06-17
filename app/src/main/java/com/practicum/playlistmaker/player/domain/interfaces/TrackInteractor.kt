package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.search.domain.entities.Track

interface TrackInteractor {
    fun getTrack(): Track
    fun startPlayer(): MediaPlayerState
    fun pausePlayer(): MediaPlayerState
    fun playbackControl(): MediaPlayerState
}