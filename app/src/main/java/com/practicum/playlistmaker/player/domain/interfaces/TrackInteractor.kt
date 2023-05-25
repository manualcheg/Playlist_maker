package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.Track

interface TrackInteractor {
    fun getTrack(): Track
    fun preparePlayer():Int
    fun startPlayer():Int
    fun pausePlayer():Int
    fun playbackControl():Int
}