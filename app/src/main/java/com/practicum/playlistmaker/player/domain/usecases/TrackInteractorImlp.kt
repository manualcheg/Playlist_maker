package com.practicum.playlistmaker.player.domain.usecases

import com.practicum.playlistmaker.player.domain.entities.MediaPlayerState
import com.practicum.playlistmaker.player.domain.entities.Track
import com.practicum.playlistmaker.player.domain.interfaces.TrackInteractor
import com.practicum.playlistmaker.player.domain.interfaces.TrackRepository

class TrackInteractorImlp(
    private val trackRepository: TrackRepository,
    var playerState: MediaPlayerState
) : TrackInteractor {
    override fun playbackControl(): MediaPlayerState {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> playerState = pausePlayer()
            MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> playerState =
                startPlayer()
            else -> {}
        }
        return playerState
    }

    override fun getTrack(): Track {
        return trackRepository.getTrack()
    }

    override fun startPlayer(): MediaPlayerState {
        playerState = trackRepository.startPlayer()
        return playerState
    }

    override fun pausePlayer(): MediaPlayerState {
        playerState = trackRepository.pausePlayer()
        return playerState
    }
}