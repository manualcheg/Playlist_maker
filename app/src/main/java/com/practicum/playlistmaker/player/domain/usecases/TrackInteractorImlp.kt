package com.practicum.playlistmaker.player.domain.usecases

import com.practicum.playlistmaker.player.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.MediaPlayerPrepare
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository

class TrackInteractorImlp(private val trackRepository: TrackRepository, var playerState:Int) : TrackInteractor {
    override fun playbackControl():Int {
        when (playerState) {
            STATE_PLAYING -> playerState = pausePlayer()
            STATE_PAUSED, STATE_PREPARED -> playerState = startPlayer()
        }
        return playerState
    }

    override fun getTrack(): Track {
        return trackRepository.getTrack()
    }

/*    override fun preparePlayer() {
        trackRepository.preparePlayer(mediaPlayerPrepare:MediaPlayerPrepare)
//        return playerState
    }*/

    override fun startPlayer():Int {
        playerState = trackRepository.startPlayer()
        return playerState
    }

    override fun pausePlayer():Int {
        playerState = trackRepository.pausePlayer()
        return playerState
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}