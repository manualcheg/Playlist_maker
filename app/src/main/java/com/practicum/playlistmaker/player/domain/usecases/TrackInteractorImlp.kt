package com.practicum.playlistmaker.player.domain.usecases

import com.practicum.playlistmaker.player.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository

class TrackInteractorImlp(private val trackRepository: TrackRepository) : TrackInteractor {
    var playerState = STATE_DEFAULT

    override fun getTrack(): Track {
        return trackRepository.getTrack()
    }

    override fun preparePlayer() {
        playerState = trackRepository.preparePlayer()
    }

    override fun startPlayer() {
        playerState = trackRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerState = trackRepository.pausePlayer()
    }

    override fun playbackControl():Int {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PAUSED, STATE_PREPARED -> startPlayer()
        }
        return playerState
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}