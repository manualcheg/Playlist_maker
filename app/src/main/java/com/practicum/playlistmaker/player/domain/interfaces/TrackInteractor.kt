package com.practicum.playlistmaker.player.domain.interfaces

import com.practicum.playlistmaker.player.domain.entities.Track

interface TrackInteractor {
    fun getTrack(): Track
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl():Int
}